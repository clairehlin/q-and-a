package com.claire.qanda.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static java.util.Arrays.asList;

class H2QuestionRepositoryTest {

    @BeforeEach
    void create_schema() throws IOException {
        applyDatabaseUpdatesFromFile("/db-schema/schema.sql");
        applyDatabaseUpdatesFromFile("questions.sql");
    }

    @Test
    void can_connect_to_db() {
        H2QuestionRepository h2QuestionRepository = new H2QuestionRepository();
        h2QuestionRepository.list();
    }

    void applyDatabaseUpdatesFromFile(String filepath) throws IOException {
        final InputStream textAsStream = this.getClass().getResourceAsStream(filepath);
        final String textContents = inputStreamToString(textAsStream);
        final String[] splitText = textContents.split(";");
        List<String> statements = asList(splitText);
        applyUpdatesToDatabase(statements);
    }

    void applyUpdatesToDatabase(List<String> schemaStatements) {
        String url = "jdbc:h2:mem:";

        try (
                Connection con = DriverManager.getConnection(url);
                Statement stm = con.createStatement()
        ) {
            for (String statement : schemaStatements) {
                stm.executeUpdate(statement);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String inputStreamToString(InputStream inputStream)
            throws IOException {

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        return textBuilder.toString();
    }
}