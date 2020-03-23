package com.claire.qanda.repository;

import com.claire.qanda.common.IO;
import com.claire.qanda.model.MultipleChoiceQuestion;
import com.claire.qanda.model.OpenQuestion;
import com.claire.qanda.model.Question;
import com.claire.qanda.model.SimpleTrueOrFalseQuestion;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

import static com.claire.qanda.repository.Database.applyDatabaseUpdatesFromFile;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class H2QuestionRepositoryTest {
    private static String dbUrl;

    @BeforeAll
    static void loadProperties() {
        final Properties dbConfig = IO.getPropertiesFromResource("config/db.properties");
        dbUrl = dbConfig.getProperty("db.url");
    }

    @Nested
    class SavingQuestions {
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

        @Test
        void can_save_true_false_question_to_database() throws IOException {
            //given
            H2QuestionRepository h2QuestionRepository = new H2QuestionRepository(dbUrl);
            applyDatabaseUpdatesFromFile(dbUrl, "questions.sql");

            //when
            SimpleTrueOrFalseQuestion simpleTrueOrFalseQuestion = new SimpleTrueOrFalseQuestion(
                    null, "you are ok.",
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
        void can_save_open_questions_to_database() throws IOException {
            //given
            H2QuestionRepository h2QuestionRepository = new H2QuestionRepository(dbUrl);
            applyDatabaseUpdatesFromFile(dbUrl, "questions.sql");

            //when
            OpenQuestion question = new OpenQuestion(null, "what day is today?", "Saturday");
            Question savedQuestion = h2QuestionRepository.save(question);

            //then
            assertEquals(h2QuestionRepository.list().size(), 5);
            final boolean savedQuestionFound = h2QuestionRepository.list()
                    .stream()
                    .anyMatch(q -> q.statement().equals("what day is today?"));
            assertTrue(savedQuestionFound);
            assertNotNull(savedQuestion.id());
        }

        @Test
        void cannot_save_question_of_unknown_type_to_database() throws IOException {
            //given
            H2QuestionRepository h2QuestionRepository = new H2QuestionRepository(dbUrl);
            applyDatabaseUpdatesFromFile(dbUrl, "questions.sql");

            //when
            //then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> h2QuestionRepository.save(new UkownQuestionType())
            );
        }
    }

    @Nested
    class DeletingQuestions {
        @Test
        void cannot_delete_question_with_non_existing_id() throws IOException {
            //given
            H2QuestionRepository h2QuestionRepository = new H2QuestionRepository(dbUrl);
            applyDatabaseUpdatesFromFile(dbUrl, "questions.sql");
            final List<Question> originalList = h2QuestionRepository.list();

            //when
            //then
            assertThrows(
                    NoSuchElementException.class,
                    () -> h2QuestionRepository.deleteQuestionWithId(7)
            );
        }

        @Test
        void can_delete_open_question_with_id() throws IOException {
            //given
            H2QuestionRepository h2QuestionRepository = new H2QuestionRepository(dbUrl);
            applyDatabaseUpdatesFromFile(dbUrl, "questions.sql");
            final List<Question> originalList = h2QuestionRepository.list();

            //when
            h2QuestionRepository.deleteQuestionWithId(2);

            //then
            assertEquals(originalList.size(), 4);
            assertEquals(h2QuestionRepository.list().size(), 3);
        }
    }

    @Nested
    class GettingQuestions {

        @Test
        void can_list_questions_from_database() throws IOException {
            //given
            H2QuestionRepository h2QuestionRepository = new H2QuestionRepository(dbUrl);
            applyDatabaseUpdatesFromFile(dbUrl, "questions.sql");

            //when
            final List<Question> questions = h2QuestionRepository.list();

            //then
            assertEquals(questions.size(), 4);
        }

        @Test
        void can_get_open_question_with_id() throws IOException {
            //given
            H2QuestionRepository h2QuestionRepository = new H2QuestionRepository(dbUrl);
            applyDatabaseUpdatesFromFile(dbUrl, "questions.sql");

            //when
            Question openQuestion = h2QuestionRepository.getQuestion(1);

            //then
            assertEquals(openQuestion.getClass(), OpenQuestion.class);
            assertEquals("how are you?", openQuestion.statement());
            assertEquals("I am well", openQuestion.correctAnswer());
        }

        @Test
        void can_get_true_or_false_question_with_id() throws IOException {
            // given
            H2QuestionRepository h2QuestionRepository = new H2QuestionRepository(dbUrl);
            applyDatabaseUpdatesFromFile(dbUrl, "questions.sql");

            //when
            Question trueOrFalseQuestion = h2QuestionRepository.getQuestion(3);

            //then
            assertEquals(trueOrFalseQuestion.getClass(), SimpleTrueOrFalseQuestion.class);
            assertEquals("you are well.\n1. true\n2. false", trueOrFalseQuestion.statement());
            assertEquals("true", trueOrFalseQuestion.correctAnswer());
        }

        @Test
        void can_get_multiple_choice_question_with_id() throws IOException {
            //given
            H2QuestionRepository h2QuestionRepository = new H2QuestionRepository(dbUrl);
            applyDatabaseUpdatesFromFile(dbUrl, "questions.sql");

            //when
            Question multipleChoiceQuestion = h2QuestionRepository.getQuestion(4);

            //then
            assertEquals(multipleChoiceQuestion.getClass(), MultipleChoiceQuestion.class);
            assertEquals("how old are you?\n1. 25 years old\n2. 26 years old", multipleChoiceQuestion.statement());
            assertEquals("25 years old", multipleChoiceQuestion.correctAnswer());
        }

        @Test
        void cannot_get_question_with_non_existing_id() throws IOException {
            //given
            H2QuestionRepository h2QuestionRepository = new H2QuestionRepository(dbUrl);
            applyDatabaseUpdatesFromFile(dbUrl, "questions.sql");

            //when
            //then
            assertThrows(NoSuchElementException.class,
                    () -> h2QuestionRepository.getQuestion(7));
        }
    }

    @Nested
    class UpdatingQuestions {
        @Test
        void can_update_open_question_in_database() throws IOException {
            //given
            H2QuestionRepository h2QuestionRepository = new H2QuestionRepository(dbUrl);
            applyDatabaseUpdatesFromFile(dbUrl, "questions.sql");
            final Question originalQuestion = h2QuestionRepository.getQuestion(2);

            //when
            OpenQuestion openQuestion = new OpenQuestion(
                    2,
                    "what is your favourite sports?",
                    "skiing"
            );
            h2QuestionRepository.updateQuestion(openQuestion);

            //then
            final Question updatedQuestion = h2QuestionRepository.getQuestion(2);
            assertNotEquals(originalQuestion.correctAnswer(), updatedQuestion.correctAnswer());
            assertEquals(updatedQuestion.correctAnswer(), "skiing");
        }

        @Test
        void can_update_true_or_false_question_in_database() throws IOException {
            //given
            H2QuestionRepository h2QuestionRepository = new H2QuestionRepository(dbUrl);
            applyDatabaseUpdatesFromFile(dbUrl, "questions.sql");
            final Question originalQuestion = h2QuestionRepository.getQuestion(3);

            //when
            SimpleTrueOrFalseQuestion simpleTrueOrFalseQuestion = new SimpleTrueOrFalseQuestion(
                    3,
                    "you are well.",
                    false
            );
            h2QuestionRepository.updateQuestion(simpleTrueOrFalseQuestion);

            //then
            final Question updatedQuestion = h2QuestionRepository.getQuestion(3);
            assertNotEquals(originalQuestion.correctAnswer(), updatedQuestion.correctAnswer());
            assertEquals(updatedQuestion.correctAnswer(), "false");
        }

        @Test
        void can_update_multiple_choice_question_in_database() throws IOException {
            //given
            H2QuestionRepository h2QuestionRepository = new H2QuestionRepository(dbUrl);
            applyDatabaseUpdatesFromFile(dbUrl, "questions.sql");
            final Question originalQuestion = h2QuestionRepository.getQuestion(4);

            //when
            MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion(
                    4,
                    "how old are you today?",
                    asList(
                            "35 years old",
                            "36 years old",
                            "37 years old"
                    ),
                    2
            );
            h2QuestionRepository.updateQuestion(multipleChoiceQuestion);

            //then
            final Question updatedQuestion = h2QuestionRepository.getQuestion(4);
            assertNotEquals(originalQuestion.correctAnswer(), updatedQuestion.correctAnswer());
            assertEquals(updatedQuestion.correctAnswer(), "37 years old");
        }

        @Test
        void cannot_update_question_of_unknown_type_in_database() throws IOException {
            //given
            H2QuestionRepository h2QuestionRepository = new H2QuestionRepository(dbUrl);
            applyDatabaseUpdatesFromFile(dbUrl, "questions.sql");

            //when
            //then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> h2QuestionRepository.updateQuestion(new UkownQuestionType())
            );
        }

        @Test
        void cannot_update_non_existing_question_in_database() throws IOException {
            //given
            H2QuestionRepository h2QuestionRepository = new H2QuestionRepository(dbUrl);
            applyDatabaseUpdatesFromFile(dbUrl, "questions.sql");

            //when
            OpenQuestion nonExistingQuestion = new OpenQuestion(
                    7,
                    "what is your favourite sports?",
                    "skiing"
            );

            //then
            assertThrows(
                    NoSuchElementException.class,
                    () -> h2QuestionRepository.updateQuestion(nonExistingQuestion)
            );
        }
    }

    // Test Utilities

    private static class UkownQuestionType implements Question {

        @Override
        public String statement() {
            return null;
        }

        @Override
        public String correctAnswer() {
            return null;
        }

        @Override
        public Integer id() {
            return null;
        }
    }
}