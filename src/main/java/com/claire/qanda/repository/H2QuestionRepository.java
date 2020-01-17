package com.claire.qanda.repository;

import com.claire.qanda.model.MultipleChoiceQuestion;
import com.claire.qanda.model.OpenQuestion;
import com.claire.qanda.model.Question;
import com.claire.qanda.model.SimpleTrueOrFalseQuestion;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class H2QuestionRepository implements QuestionRepository {

    @Override
    public Question save(Question question) {
        return null;
    }

    @Override
    public List<Question> list() {
        List<Question> questions = new ArrayList<>();
        questions.addAll(openQuestions());
        questions.addAll(trueOrFalseQuestions());
        questions.addAll(multipleChoiceQuestions());
        return questions;
    }

    private Collection<? extends Question> multipleChoiceQuestions() {
        String tableName = "multiple_choice_question";
        List<Question> questions = new ArrayList<>();

        String url = "jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1";
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
                        initialPhrase,
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

        String url = "jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1";
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

        String url = "jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1";
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

        String url = "jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1";
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
                        initialPhrase,
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

        String url = "jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1";
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
                OpenQuestion openQuestion = new OpenQuestion(statement, answer);
                openQuestions.add(openQuestion);

            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return openQuestions;
    }
}
