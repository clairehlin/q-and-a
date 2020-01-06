package com.claire.qanda;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimpleTrueOrFalseQuestionTest {

    @Test
    void can_create_question_successfully() {
        assertDoesNotThrow(() ->
                trueOrFalseQuestion("Trump is the current president.")
        );
        System.out.println(trueOrFalseQuestion("Trump is the current president.").statement());
    }

    @Test
    void should_not_accept_empty_statement() {
        assertThrows(
                IllegalArgumentException.class,
                () -> trueOrFalseQuestion("")
        );

        assertThrows(
                NullPointerException.class,
                () -> trueOrFalseQuestion(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> trueOrFalseQuestion(" ")
        );
    }

    @Test
    void should_not_accept_question_mark_on_statement() {
        assertThrows(
                IllegalArgumentException.class,
                () -> trueOrFalseQuestion("There is only one solar system?")
        );
    }

    @Test
    void statement_should_end_with_full_stop() {
        assertThrows(
                IllegalArgumentException.class,
                () -> trueOrFalseQuestion("There is only one solar system")
        );
    }

    private Question trueOrFalseQuestion(String s) {
//        return new SimpleTrueOrFalseQuestion(s, true);
//        return new CompositionTrueOrFalseQuestion(s, true);
        return new InheritanceTrueOrFalseQuestion(s, true);
    }
}