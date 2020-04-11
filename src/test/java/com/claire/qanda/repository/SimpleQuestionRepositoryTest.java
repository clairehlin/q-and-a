package com.claire.qanda.repository;

import com.claire.qanda.model.MultipleChoiceQuestion;
import com.claire.qanda.model.OpenQuestion;
import com.claire.qanda.model.Question;
import com.claire.qanda.model.SimpleTrueOrFalseQuestion;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleQuestionRepositoryTest {

    @Nested
    class SavingQuestions {
        @Test
        void can_save_multiple_choice_questions() throws IOException {
            //given
            SimpleQuestionRepository simpleQuestionRepository = new SimpleQuestionRepository();
            initializeRepository(simpleQuestionRepository);

            //when
            MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion(
                    "what is the colour of ocean?",
                    asList("green", "blue"),
                    1
            );
            Question questions = simpleQuestionRepository.save(multipleChoiceQuestion);

            //then
            assertEquals(simpleQuestionRepository.list().size(), 5);
            final boolean savedQuestionsFound = simpleQuestionRepository.list()
                    .stream()
                    .anyMatch(q -> q.statement().startsWith("what is the colour of ocean?"));
            assertTrue(savedQuestionsFound);
        }

        @Test
        void can_save_true_false_question_to_database() throws IOException {
            //given
            SimpleQuestionRepository simpleQuestionRepository = new SimpleQuestionRepository();
            initializeRepository(simpleQuestionRepository);

            //when
            SimpleTrueOrFalseQuestion simpleTrueOrFalseQuestion = new SimpleTrueOrFalseQuestion(
                    null, "you are ok.",
                    true
            );
            Question questions = simpleQuestionRepository.save(simpleTrueOrFalseQuestion);

            //then
            assertEquals(simpleQuestionRepository.list().size(), 5);
            final boolean savedQuestionFound = simpleQuestionRepository.list()
                    .stream()
                    .anyMatch(q -> q.statement().startsWith("you are ok."));
            assertTrue(savedQuestionFound);
        }

        @Test
        void can_save_open_questions_to_database() throws IOException {
            //given
            SimpleQuestionRepository simpleQuestionRepository = new SimpleQuestionRepository();
            initializeRepository(simpleQuestionRepository);

            //when
            OpenQuestion question = new OpenQuestion(null, "what day is today?", "Saturday");
            Question savedQuestion = simpleQuestionRepository.save(question);

            //then
            assertEquals(simpleQuestionRepository.list().size(), 5);
            final boolean savedQuestionFound = simpleQuestionRepository.list()
                    .stream()
                    .anyMatch(q -> q.statement().equals("what day is today?"));
            assertTrue(savedQuestionFound);
            assertNotNull(savedQuestion.id());
        }

        @Test
        void cannot_save_question_of_unknown_type_to_database() throws IOException {
            //given
            SimpleQuestionRepository simpleQuestionRepository = new SimpleQuestionRepository();
            initializeRepository(simpleQuestionRepository);

            //when
            //then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> simpleQuestionRepository.save(new UkownQuestionType())
            );
        }
    }

    private void initializeRepository(SimpleQuestionRepository simpleQuestionRepository) {
        simpleQuestionRepository.save(new OpenQuestion(1, "how are you?", "I am well"));
        simpleQuestionRepository.save(new OpenQuestion(2, "where are you?", "hidden"));
        simpleQuestionRepository.save(new SimpleTrueOrFalseQuestion(3, "you are well.", true));
        simpleQuestionRepository.save(
                new MultipleChoiceQuestion(
                        4,
                        "how old are you?",
                        asList(
                                "25 years old",
                                "26 years old"
                        ),
                        0
                )
        );
    }

    @Nested
    class DeletingQuestions {
        @Test
        void cannot_delete_question_with_non_existing_id() throws IOException {
            //given
            SimpleQuestionRepository simpleQuestionRepository = new SimpleQuestionRepository();
            initializeRepository(simpleQuestionRepository);

            final List<Question> originalList = simpleQuestionRepository.list();

            //when
            //then
            assertThrows(
                    NoSuchElementException.class,
                    () -> simpleQuestionRepository.deleteQuestionWithId(7)
            );
        }

        @Test
        void can_delete_open_question_with_id() throws IOException {
            //given
            SimpleQuestionRepository simpleQuestionRepository = new SimpleQuestionRepository();
            initializeRepository(simpleQuestionRepository);

            final List<Question> originalList = simpleQuestionRepository.list();

            //when
            Question openQuestion = getQuestionOfType(OpenQuestion.class, originalList);
            simpleQuestionRepository.deleteQuestionWithId(openQuestion.id());

            //then
            assertEquals(openQuestion.getClass(), OpenQuestion.class);
            assertTrue(
                    simpleQuestionRepository.list()
                            .stream()
                            .map(Question::id)
                            .noneMatch(id -> id.equals(openQuestion.id()))
            );
        }

        @Test
        void can_delete_true_or_false_question_with_id() throws IOException {
            //given
            SimpleQuestionRepository simpleQuestionRepository = new SimpleQuestionRepository();
            initializeRepository(simpleQuestionRepository);

            final List<Question> originalList = simpleQuestionRepository.list();

            //when
            Question simpleTrueOrFalseQuestion = getQuestionOfType(SimpleTrueOrFalseQuestion.class, originalList);
            simpleQuestionRepository.deleteQuestionWithId(simpleTrueOrFalseQuestion.id());

            //then
            assertEquals(simpleTrueOrFalseQuestion.getClass(), SimpleTrueOrFalseQuestion.class);
            assertTrue(
                    simpleQuestionRepository.list()
                            .stream()
                            .map(Question::id)
                            .noneMatch(id -> id.equals(simpleTrueOrFalseQuestion.id()))
            );
        }

        @Test
        void can_delete_multiple_choice_question_with_id() throws IOException {
            //given
            SimpleQuestionRepository simpleQuestionRepository = new SimpleQuestionRepository();
            initializeRepository(simpleQuestionRepository);

            final List<Question> originalList = simpleQuestionRepository.list();

            //when
            Question multipleChoiceQuestion = getQuestionOfType(MultipleChoiceQuestion.class, originalList);
            simpleQuestionRepository.deleteQuestionWithId(multipleChoiceQuestion.id());

            //then
            assertEquals(multipleChoiceQuestion.getClass(), MultipleChoiceQuestion.class);
            assertTrue(
                    simpleQuestionRepository.list()
                            .stream()
                            .map(Question::id)
                            .noneMatch(id -> id.equals(multipleChoiceQuestion.id()))
            );
        }

        private Question getQuestionOfType(Class<? extends Question> questionClass, List<Question> originalList) {
            return originalList.stream()
                    .filter(question -> question.getClass() == questionClass)
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);
        }
    }

    @Nested
    class GettingQuestions {

        @Test
        void can_list_questions_from_database() throws IOException {
            //given
            SimpleQuestionRepository simpleQuestionRepository = new SimpleQuestionRepository();
            initializeRepository(simpleQuestionRepository);

            //when
            final List<Question> questions = simpleQuestionRepository.list();

            //then
            assertEquals(questions.size(), 4);
        }

        @Test
        void can_get_open_question_with_id() throws IOException {
            //given
            SimpleQuestionRepository simpleQuestionRepository = new SimpleQuestionRepository();
            initializeRepository(simpleQuestionRepository);

            //when
            Question openQuestion = simpleQuestionRepository.getQuestion(1);

            //then
            assertEquals(openQuestion.getClass(), OpenQuestion.class);
            assertEquals("how are you?", openQuestion.statement());
            assertEquals("I am well", openQuestion.correctAnswer());
        }

        @Test
        void can_get_true_or_false_question_with_id() throws IOException {
            // given
            SimpleQuestionRepository simpleQuestionRepository = new SimpleQuestionRepository();
            initializeRepository(simpleQuestionRepository);

            //when
            Question trueOrFalseQuestion = simpleQuestionRepository.getQuestion(3);

            //then
            assertEquals(trueOrFalseQuestion.getClass(), SimpleTrueOrFalseQuestion.class);
            assertEquals("you are well.\n1. true\n2. false", trueOrFalseQuestion.statement());
            assertEquals("true", trueOrFalseQuestion.correctAnswer());
        }

        @Test
        void can_get_multiple_choice_question_with_id() throws IOException {
            //given
            SimpleQuestionRepository simpleQuestionRepository = new SimpleQuestionRepository();
            initializeRepository(simpleQuestionRepository);

            //when
            Question multipleChoiceQuestion = simpleQuestionRepository.getQuestion(4);

            //then
            assertEquals(multipleChoiceQuestion.getClass(), MultipleChoiceQuestion.class);
            assertEquals("how old are you?\n1. 25 years old\n2. 26 years old", multipleChoiceQuestion.statement());
            assertEquals("25 years old", multipleChoiceQuestion.correctAnswer());
        }

        @Test
        void cannot_get_question_with_non_existing_id() throws IOException {
            //given
            SimpleQuestionRepository simpleQuestionRepository = new SimpleQuestionRepository();
            initializeRepository(simpleQuestionRepository);

            //when
            //then
            assertThrows(
                    NoSuchElementException.class,
                    () -> simpleQuestionRepository.getQuestion(7)
            );
        }
    }

    @Nested
    class UpdatingQuestions {
        @Test
        void can_update_open_question_in_database() throws IOException {
            //given
            SimpleQuestionRepository simpleQuestionRepository = new SimpleQuestionRepository();
            initializeRepository(simpleQuestionRepository);

            final Question originalQuestion = simpleQuestionRepository.getQuestion(2);

            //when
            OpenQuestion openQuestion = new OpenQuestion(
                    2,
                    "what is your favourite sports?",
                    "skiing"
            );
            simpleQuestionRepository.updateQuestion(openQuestion);

            //then
            final Question updatedQuestion = simpleQuestionRepository.getQuestion(2);
            assertNotEquals(originalQuestion.correctAnswer(), updatedQuestion.correctAnswer());
            assertEquals(updatedQuestion.correctAnswer(), "skiing");
        }

        @Test
        void can_update_true_or_false_question_in_database() throws IOException {
            //given
            SimpleQuestionRepository simpleQuestionRepository = new SimpleQuestionRepository();
            initializeRepository(simpleQuestionRepository);

            final Question originalQuestion = simpleQuestionRepository.getQuestion(3);

            //when
            SimpleTrueOrFalseQuestion simpleTrueOrFalseQuestion = new SimpleTrueOrFalseQuestion(
                    3,
                    "you are well.",
                    false
            );
            simpleQuestionRepository.updateQuestion(simpleTrueOrFalseQuestion);

            //then
            final Question updatedQuestion = simpleQuestionRepository.getQuestion(3);
            assertNotEquals(originalQuestion.correctAnswer(), updatedQuestion.correctAnswer());
            assertEquals(updatedQuestion.correctAnswer(), "false");
        }

        @Test
        void can_update_multiple_choice_question_in_database() throws IOException {
            //given
            SimpleQuestionRepository simpleQuestionRepository = new SimpleQuestionRepository();
            initializeRepository(simpleQuestionRepository);

            final Question originalQuestion = simpleQuestionRepository.getQuestion(4);

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
            simpleQuestionRepository.updateQuestion(multipleChoiceQuestion);

            //then
            final Question updatedQuestion = simpleQuestionRepository.getQuestion(4);
            assertNotEquals(originalQuestion.correctAnswer(), updatedQuestion.correctAnswer());
            assertEquals(updatedQuestion.correctAnswer(), "37 years old");
        }

        @Test
        void cannot_update_question_of_unknown_type_in_database() throws IOException {
            //given
            SimpleQuestionRepository simpleQuestionRepository = new SimpleQuestionRepository();
            initializeRepository(simpleQuestionRepository);

            //when
            //then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> simpleQuestionRepository.updateQuestion(new UkownQuestionType())
            );
        }

        @Test
        void cannot_update_non_existing_question_in_database() throws IOException {
            //given
            SimpleQuestionRepository simpleQuestionRepository = new SimpleQuestionRepository();
            initializeRepository(simpleQuestionRepository);

            //when
            OpenQuestion nonExistingQuestion = new OpenQuestion(
                    7,
                    "what is your favourite sports?",
                    "skiing"
            );

            //then
            assertThrows(
                    NoSuchElementException.class,
                    () -> simpleQuestionRepository.updateQuestion(nonExistingQuestion)
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