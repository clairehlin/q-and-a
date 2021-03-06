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
    public Question save(Question question) {
        if (question.getClass() == OpenQuestion.class) {
            return saveOpenQuestion((OpenQuestion) question);
        } else if (question.getClass() == SimpleTrueOrFalseQuestion.class) {
            return saveSimpleTrueOrFalseQuestion((SimpleTrueOrFalseQuestion) question);
        } else if (question.getClass() == MultipleChoiceQuestion.class) {
            return saveMultipleChoiceQuestion((MultipleChoiceQuestion) question);
        } else {
            throw new IllegalArgumentException("cannot save question of type " + question.getClass().getName());
        }
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

    private Question getOpenQuestion(Integer id) {
        String sql = "select * from open_question where id = ?";

        try (
                Connection con = DriverManager.getConnection(url);
                PreparedStatement stm = con.prepareStatement(sql)
        ) {
            stm.setInt(1, id);

            final ResultSet resultSet = stm.executeQuery();

            if (resultSet.next()) {
                return new OpenQuestion(id, resultSet.getString("statement"), resultSet.getString("answer"));
            } else {
                return null;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private boolean deleteOpenQuestionSuccessful(Integer id) {
        String sql = "delete from open_question where id = ?";

        try (
                Connection con = DriverManager.getConnection(url);
                PreparedStatement stm = con.prepareStatement(sql)
        ) {
            stm.setInt(1, id);
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void deleteQuestionWithId(Integer id) {
        if (deleteOpenQuestionSuccessful(id)){
            return;
        } else if (deleteTrueOrFalseQuestionSuccessful(id)) {
            return;
        } else if (deleteMultipleChoiceQuestionSuccessful(id)) {
            return;
        } else {
            throw new NoSuchElementException("cannot find question with id: " + id);
        }
    }

    private boolean deleteMultipleChoiceQuestionSuccessful(Integer id) {
        deleteChoicesForQuestionWithId(id);
        String sql = "delete from multiple_choice_question where id = ?";

        try (
                Connection con = DriverManager.getConnection(url);
                PreparedStatement stm = con.prepareStatement(sql)
        ) {
            stm.setInt(1, id);
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private boolean deleteTrueOrFalseQuestionSuccessful(Integer id) {
        String sql = "delete from true_false_question where id = ?";

        try (
                Connection con = DriverManager.getConnection(url);
                PreparedStatement stm = con.prepareStatement(sql)
        ) {
            stm.setInt(1, id);
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Question getQuestion(Integer id) {
        Question openQuestion = getOpenQuestion(id);
        if (openQuestion != null) {
            return openQuestion;
        } else {
            Question trueOrFalseQuestion = getTrueOrFalseQuestion(id);
            if (trueOrFalseQuestion != null) {
                return trueOrFalseQuestion;
            } else {
                Question multipleChoiceQuestion = getMultipleChoiceQuestion(id);
                if (multipleChoiceQuestion != null) {
                    return multipleChoiceQuestion;
                } else {
                    throw new NoSuchElementException("cannot find question with id: " + id);
                }
            }
        }
    }

    @Override
    public void updateQuestion(Question question) {
        int countUpdated;
        if (question.getClass() == OpenQuestion.class) {
            countUpdated = updateOpenQuestion((OpenQuestion) question);
        } else if (question.getClass() == SimpleTrueOrFalseQuestion.class) {
            countUpdated = updateSimpleTrueOrFalseQuestion((SimpleTrueOrFalseQuestion) question);
        } else if (question.getClass() == MultipleChoiceQuestion.class) {
            countUpdated = updateMultipleChoiceQuestion((MultipleChoiceQuestion) question);
        } else {
            throw new IllegalArgumentException("cannot update question of type " + question.getClass().getName());
        }
        if (countUpdated < 1) {
            throw new NoSuchElementException("cannot find question with id " + question.id());
        }
    }

    private int updateOpenQuestion(OpenQuestion question) {
        {
            String sql = "update open_question set statement = ?, answer = ? where id = ?";

            try (
                    Connection con = DriverManager.getConnection(url);
                    PreparedStatement stm = con.prepareStatement(sql)
            ) {
                stm.setString(1, question.statement());
                stm.setString(2, question.correctAnswer());
                stm.setInt(3, question.id());
                return stm.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private int updateMultipleChoiceQuestion(MultipleChoiceQuestion question) {
        String sql = "update multiple_choice_question set initial_phrase = ? where id = ? ";

        try (
                Connection con = DriverManager.getConnection(url);
                PreparedStatement stm = con.prepareStatement(sql)
        ) {
            //setString(parameterIndex: 1, ....) means replace the above one (?) with the value in
            // the second parameter which is question.getInitialPhrase()
            stm.setString(1, question.getInitialPhrase());
            stm.setInt(2, question.id());
            deleteChoicesForQuestionWithId(question.id());
            saveChoices(con, question.id(), question.getChoices(), question.correctAnswer());
            return stm.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void deleteChoicesForQuestionWithId(Integer id) {
        String sql = "delete from choice where multiple_choice_question_id = ?";

        try (
                Connection con = DriverManager.getConnection(url);
                PreparedStatement stm = con.prepareStatement(sql)
        ) {
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private int updateSimpleTrueOrFalseQuestion(SimpleTrueOrFalseQuestion question) {
        {
            String sql = "update true_false_question set initial_phrase = ?, answer = ? where id = ?";

            try (
                    Connection con = DriverManager.getConnection(url);
                    PreparedStatement stm = con.prepareStatement(sql)
            ) {
                stm.setString(1, question.getInitialPhrase());
                stm.setString(2, question.correctAnswer());
                stm.setInt(3, question.id());
                return stm.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private Question getMultipleChoiceQuestion(Integer id) {
        String sql = "select * from multiple_choice_question where id = ?";
        try (
                Connection con = DriverManager.getConnection(url);
                PreparedStatement stm = con.prepareStatement(sql)
        ) {
            stm.setInt(1, id);

            final ResultSet resultSet = stm.executeQuery();

            if (resultSet.next()) {
                String initialPhrase = resultSet.getString(2);
                List<String> choices = getChoices(id);
                int answer = getAnswer(id);
                MultipleChoiceQuestion question = new MultipleChoiceQuestion(
                        id, initialPhrase,
                        choices,
                        answer
                );
                return question;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Question getTrueOrFalseQuestion(Integer id) {
        String sql = "select * from true_false_question where id = ?";

        try (
                Connection con = DriverManager.getConnection(url);
                PreparedStatement stm = con.prepareStatement(sql)
        ) {
            stm.setInt(1, id);

            final ResultSet resultSet = stm.executeQuery();

            if (resultSet.next()) {
                return new SimpleTrueOrFalseQuestion(
                        id,
                        resultSet.getString("initial_phrase"),
                        resultSet.getBoolean("answer")
                );
            } else {
                return null;
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
