package com.claire.qanda;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuestionTest {

    @Test
    void should_not_accept_empty_statement() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Question("", Question.QType.OPEN)
        );
    }

    @Test
    void should_not_accept_null_statement() {
        assertThrows(
                NullPointerException.class,
                () -> new Question(null, Question.QType.OPEN)
        );
    }

    @Test
    void should_not_accept_blank_statement() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Question("   \t\n", Question.QType.OPEN)
        );
    }

    @Test
    void should_accept_non_blank_statement() {
        assertDoesNotThrow(
                () -> new Question("abc", Question.QType.OPEN)
        );
    }

    @Test
    void should_require_question_type() {
        assertThrows(
                NullPointerException.class,
                () -> new Question("abc", null)
        );
    }

}

