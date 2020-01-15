package com.claire.qanda.repository;

import com.claire.qanda.model.OpenQuestion;
import com.claire.qanda.model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
        return Collections.emptyList();
    }

    private Collection<? extends Question> trueOrFalseQuestions() {
        return Collections.emptyList();
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

                System.out.println(id);
                System.out.println(statement);
                System.out.println(answer);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return openQuestions;
    }

    private void random(){
        String url = "jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1";

        try (
                Connection con = DriverManager.getConnection(url);
                Statement stm = con.createStatement();
                ResultSet resultSet = stm.executeQuery("SELECT 1+1")
        ) {

            if (resultSet.next()) {

                System.out.println(resultSet.getInt(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }
}
