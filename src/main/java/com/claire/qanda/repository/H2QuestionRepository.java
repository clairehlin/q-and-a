package com.claire.qanda.repository;

import com.claire.qanda.model.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import static com.claire.qanda.repository.Database.applyDatabaseUpdatesFromFile;

public class H2QuestionRepository implements QuestionRepository {

    private final String url;

    public H2QuestionRepository(String dbUrl) throws DatabaseException {
        this.url = dbUrl;
        try {
            applyDatabaseUpdatesFromFile(dbUrl, "db-schema/schema.sql");
        } catch (IOException e) {
            throw new DatabaseException("Failed to create schema", e);
        }
    }

    @Override
    public IdentifiableQuestion save(IdentifiableQuestion question) {
        if (question.getClass() == OpenQuestion.class) {
            return saveOpenQuestion((OpenQuestion) question);
        } else if (question.getClass() == SimpleTrueOrFalseQuestion.class) {
            saveSimpleTrueOrFalseQuestion((SimpleTrueOrFalseQuestion) question);
        } else if (question.getClass() == MultipleChoiceQuestion.class) {
            saveMultipleChoiceQuestion((MultipleChoiceQuestion) question);
        } else {
            throw new IllegalArgumentException("cannot save question of type " + question.getClass().getName());
        }
        return new QuestionToIdentifiableQuestionAdapter(question);
    }

    private MultipleChoiceQuestion saveMultipleChoiceQuestion(MultipleChoiceQuestion question) {
        String sql = "insert into multiple_choice_question (initial_phrase) values (?)";

        try (
                Connection con = DriverManager.getConnection(url);
                PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            //setString(parameterIndex: 1, ....) means replace the above one (?) with the value in
            // the second parameter which is question.getInitialPhrase()
            stm.setString(1, question.getInitialPhrase());
            stm.executeUpdate();
            final ResultSet generatedKeys = stm.getGeneratedKeys();
            if (generatedKeys.next()) {
                final int qId = generatedKeys.getInt(1);
                saveChoices(con, qId, question.getChoices(), question.correctAnswer());
                return question.withId(qId);
            } else {
                throw new IllegalStateException("could not save question initial phrase: " + question);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void saveChoices(Connection con, int qId, List<String> choices, String correctAnswer) {
        for (String choice : choices) {
            saveChoice(con, qId, choice, choice.equals(correctAnswer));
        }
    }

    private void saveChoice(Connection con, int qId, String choice, boolean correctAnswer) {
        String sql = String.format(
                "insert into choice (multiple_choice_question_id, choice, is_correct_answer) values (%s, '%s', %s)",
                qId,
                choice,
                correctAnswer
        );

        try (
                Statement stm = con.createStatement()
        ) {
            stm.executeUpdate(sql);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private SimpleTrueOrFalseQuestion saveSimpleTrueOrFalseQuestion(SimpleTrueOrFalseQuestion question) {
        String sql = "insert into true_false_question (initial_phrase, answer) values (?, ?)";

        try (
                Connection con = DriverManager.getConnection(url);
                PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            //setString(parameterIndex: 1, ....) means replace the above one (?) with the value in
            // the second parameter which is question.statement()
            stm.setString(1, question.getInitialPhrase());
            stm.setBoolean(2, Boolean.parseBoolean(question.correctAnswer()));
            stm.executeUpdate();
            final ResultSet generatedKeys = stm.getGeneratedKeys();

            if (generatedKeys.next()) {
                final int qId = generatedKeys.getInt(1);
                return question.withId(qId);
            } else {
                throw new IllegalStateException("could not find generated keys");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private OpenQuestion saveOpenQuestion(OpenQuestion question) {
        String sql = "insert into open_question (statement, answer) values (?, ?)";

        try (
                Connection con = DriverManager.getConnection(url);
                PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            //setString(parameterIndex: 1, ....) means replace the above one (?) with the value in
            // the second parameter which is question.statement()
            stm.setString(1, question.statement());
            stm.setString(2, question.correctAnswer());
            stm.executeUpdate();
            final ResultSet generatedKeys = stm.getGeneratedKeys();

            if (generatedKeys.next()) {
                final int qId = generatedKeys.getInt(1);
                return question.withId(qId);
            } else {
                throw new IllegalStateException("could not find generated keys");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Question> list() {
        List<Question> questions = new ArrayList<>();
        questions.addAll(openQuestions());
        questions.addAll(trueOrFalseQuestions());
        questions.addAll(multipleChoiceQuestions());
        return questions;
    }

    @Override
    public IdentifiableQuestion getOpenQuestion(Integer id) {
        String sql = "select * from open_question where id=?";

        try (
                Connection con = DriverManager.getConnection(url);
                PreparedStatement stm = con.prepareStatement(sql)
        ) {
            stm.setInt(1, id);

            final ResultSet resultSet = stm.executeQuery();

            if (resultSet.next()) {
                return new OpenQuestion(id, resultSet.getString("statement"), resultSet.getString("answer"));
            } else {
                throw new NoSuchElementException("cannot find open_question with id " + id);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Collection<? extends Question> multipleChoiceQuestions() {
        String tableName = "multiple_choice_question";
        List<Question> questions = new ArrayList<>();

        String sql = "select * from " + tableName;
        try (
                Connection con = DriverManager.getConnection(url);
                Statement stm = con.createStatement();
                ResultSet resultSet = stm.executeQuery(sql)
        ) {

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String initialPhrase = resultSet.getString(2);
                List<String> choices = getChoices(id);
                int answer = getAnswer(id);
                MultipleChoiceQuestion question = new MultipleChoiceQuestion(
                        id, initialPhrase,
                        choices,
                        answer
                );
                questions.add(question);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return questions;
    }

    private int getAnswer(int id) {
        String tableName = "choice";
        int correctAnswerIndex = 0;

        String sql = "select * from " + tableName + " where multiple_choice_question_id = " + id;
        try (
                Connection con = DriverManager.getConnection(url);
                Statement stm = con.createStatement();
                ResultSet resultSet = stm.executeQuery(sql)
        ) {

            while (resultSet.next()) {
                boolean isCorrect = resultSet.getBoolean(4);
                if (isCorrect) {
                    break;
                }
                correctAnswerIndex++;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return correctAnswerIndex;
    }

    private List<String> getChoices(int id) {
        String tableName = "choice";
        List<String> choices = new ArrayList<>();

        String sql = "select * from " + tableName + " where multiple_choice_question_id = " + id;
        try (
                Connection con = DriverManager.getConnection(url);
                Statement stm = con.createStatement();
                ResultSet resultSet = stm.executeQuery(sql)
        ) {

            while (resultSet.next()) {
                String choice = resultSet.getString(3);
                choices.add(choice);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return choices;
    }

    private Collection<? extends Question> trueOrFalseQuestions() {
        String tableName = "true_false_question";
        List<Question> questions = new ArrayList<>();

        String sql = "select * from " + tableName;
        try (
                Connection con = DriverManager.getConnection(url);
                Statement stm = con.createStatement();
                ResultSet resultSet = stm.executeQuery(sql)
        ) {

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String initialPhrase = resultSet.getString(2);
                boolean answer = resultSet.getBoolean(3);
                SimpleTrueOrFalseQuestion question = new SimpleTrueOrFalseQuestion(
                        id, initialPhrase,
                        answer
                );
                questions.add(question);

            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return questions;
    }

    private Collection<? extends Question> openQuestions() {
        String openQuestionsTable = "open_question";
        List<Question> openQuestions = new ArrayList<>();

        String sql = "select * from " + openQuestionsTable;
        try (
                Connection con = DriverManager.getConnection(url);
                Statement stm = con.createStatement();

                ResultSet resultSet = stm.executeQuery(sql)
        ) {

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String statement = resultSet.getString(2);
                String answer = resultSet.getString(3);
                OpenQuestion openQuestion = new OpenQuestion(id, statement, answer);
                openQuestions.add(openQuestion);

            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return openQuestions;
    }
}
