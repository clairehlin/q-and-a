package com.claire.qanda.repository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static com.claire.qanda.common.IO.inputStreamToString;
import static java.util.Arrays.asList;

class Database {
    static void applyUpdatesToDatabase(String url, List<String> schemaStatements) {

        try (
                Connection con = DriverManager.getConnection(url);
                Statement stm = con.createStatement()
        ) {
            for (String statement : schemaStatements) {
                stm.executeUpdate(statement);
            }
            con.commit();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    static void applyDatabaseUpdatesFromFile(String dbUrl, String filepath) throws IOException {
        final InputStream textAsStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(filepath);
        final String textContents = inputStreamToString(textAsStream);
        final String[] splitText = textContents.split(";");
        List<String> statements = asList(splitText);
        applyUpdatesToDatabase(dbUrl, statements);
    }
}
