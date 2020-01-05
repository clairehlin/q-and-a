package com.claire.qanda;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TrueOrFalseQuestionTest {

    @Test
    void can_create_question_successfully() {
        assertDoesNotThrow(() ->
                new TrueOrFalseQuestion(
                        "Trump is the current president.",
                        true)
        );
    }

    @Test
    void should_not_accept_empty_statement() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new TrueOrFalseQuestion("", true)
        );

        assertThrows(
                NullPointerException.class,
                () -> new TrueOrFalseQuestion(null, true)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new TrueOrFalseQuestion(" ", true)
        );
    }

    @Test
    void should_not_accept_question_mark_on_statement() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new TrueOrFalseQuestion("There is only one solar system?", true)
        );
    }

    @Test
    void statement_should_end_with_full_stop() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new TrueOrFalseQuestion("There is only one solar system", true)
        );
    }
}