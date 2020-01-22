package com.claire.qanda.repository;

import com.claire.qanda.model.MultipleChoiceQuestion;
import com.claire.qanda.model.OpenQuestion;
import com.claire.qanda.model.Question;
import com.claire.qanda.model.SimpleTrueOrFalseQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sun.java2d.pipe.SpanShapeRenderer;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class H2QuestionRepositoryTest {

    @BeforeEach
    void create_schema() throws IOException {
        applyDatabaseUpdatesFromFile("/db-schema/schema.sql");
        applyDatabaseUpdatesFromFile("/questions.sql");
    }

    @Test
    void can_list_questions_from_database() {
        // given
        H2QuestionRepository h2QuestionRepository = new H2QuestionRepository();

        // when
        final List<Question> questions = h2QuestionRepository.list();

        // then
        assertEquals(questions.size(), 4);
    }

    @Test
    void can_save_open_questions_to_database() {
        //given
        H2QuestionRepository h2QuestionRepository = new H2QuestionRepository();

        //when
        OpenQuestion question = new OpenQuestion("what day is today?", "Saturday");
        Question questions = h2QuestionRepository.save(question);

        //then
        assertEquals(h2QuestionRepository.list().size(), 5);
        final boolean savedQuestionFound = h2QuestionRepository.list()
                .stream()
                .anyMatch(q -> q.statement().equals("what day is today?"));
        assertTrue(savedQuestionFound);
    }

    @Test
    void can_save_true_false_question_to_database() {
        //given
        H2QuestionRepository h2QuestionRepository = new H2QuestionRepository();

        //when
        SimpleTrueOrFalseQuestion simpleTrueOrFalseQuestion = new SimpleTrueOrFalseQuestion(
                "you are ok.",
                true
        );
        Question questions = h2QuestionRepository.save(simpleTrueOrFalseQuestion);

        //then
        assertEquals(h2QuestionRepository.list().size(), 5);
        final boolean savedQuestionFound = h2QuestionRepository.list()
                .stream()
                .anyMatch(q -> q.statement().startsWith("you are ok."));
        assertTrue(savedQuestionFound);
    }

    @Test
    void can_save_multiple_choice_questions() {
        //given
        H2QuestionRepository h2QuestionRepository = new H2QuestionRepository();

        //when
        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion(
                "what is the colour of ocean?",
                asList("green", "blue"),
                1
        );
        Question questions = h2QuestionRepository.save(multipleChoiceQuestion);

        //then
        assertEquals(h2QuestionRepository.list().size(), 5);
        final boolean savedQuestionsFound = h2QuestionRepository.list()
                .stream()
                .anyMatch(q -> q.statement().startsWith("what is the colour of ocean?"));
        assertTrue(savedQuestionsFound);
    }

    void applyDatabaseUpdatesFromFile(String filepath) throws IOException {
        final InputStream textAsStream = this.getClass().getResourceAsStream(filepath);
        final String textContents = inputStreamToString(textAsStream);
        final String[] splitText = textContents.split(";");
        List<String> statements = asList(splitText);
        applyUpdatesToDatabase(statements);
    }

    void applyUpdatesToDatabase(List<String> schemaStatements) {
        String url = "jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1";

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