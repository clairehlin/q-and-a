package com.claire.qanda.repository;

import com.claire.qanda.model.Question;

import java.sql.*;
import java.util.List;

public class H2QuestionRepository implements QuestionRepository {

    @Override
    public Question save(Question question) {
        return null;
    }

    @Override
    public List<Question> list() {
        String url = "jdbc:h2:mem:";

        try (
                Connection con = DriverManager.getConnection(url);
                Statement stm = con.createStatement();
                ResultSet resultSet = stm.executeQuery("SELECT 1+1")
        ) {

            if (resultSet.next()) {

                System.out.println(resultSet.getInt(1));
            }
            return null;

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
