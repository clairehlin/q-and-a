package com.claire.qanda.repository;

import com.claire.qanda.common.IO;
import com.claire.qanda.model.MultipleChoiceQuestion;
import com.claire.qanda.model.OpenQuestion;
import com.claire.qanda.model.Question;
import com.claire.qanda.model.SimpleTrueOrFalseQuestion;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static com.claire.qanda.repository.Database.applyDatabaseUpdatesFromFile;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class H2QuestionRepositoryTest {
    private static String dbUrl;

    @BeforeAll
    static void loadProperties(){
        final Properties dbConfig = IO.getPropertiesFromResource("config/db.properties");
        dbUrl = dbConfig.getProperty("db.url");
    }

    @Test
    void can_list_questions_from_database() throws IOException {
        // given
        H2QuestionRepository h2QuestionRepository = new H2QuestionRepository(dbUrl);
        applyDatabaseUpdatesFromFile(dbUrl, "questions.sql");

        // when
        final List<Question> questions = h2QuestionRepository.list();

        // then
        assertEquals(questions.size(), 4);
    }

    @Test
    void can_save_open_questions_to_database() throws IOException {
        //given
        H2QuestionRepository h2QuestionRepository = new H2QuestionRepository(dbUrl);
        applyDatabaseUpdatesFromFile(dbUrl, "questions.sql");

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
    void can_save_true_false_question_to_database() throws IOException {
        //given
        H2QuestionRepository h2QuestionRepository = new H2QuestionRepository(dbUrl);
        applyDatabaseUpdatesFromFile(dbUrl, "questions.sql");

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
    void can_save_multiple_choice_questions() throws IOException {
        //given
        H2QuestionRepository h2QuestionRepository = new H2QuestionRepository(dbUrl);
        applyDatabaseUpdatesFromFile(dbUrl, "questions.sql");

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
}