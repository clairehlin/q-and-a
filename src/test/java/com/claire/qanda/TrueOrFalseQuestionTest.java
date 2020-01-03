package com.claire.qanda;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrueOrFalseQuestionTest {

    @Test
    void can_create_question_successfully() {
        assertDoesNotThrow(() ->
                new TrueOrFalseQuestion(
                        "Trump is the current president."
                )
        );
    }

    @Test
    void should_not_accept_empty_statement(){
        assertThrows(
                IllegalArgumentException.class,
                () -> new TrueOrFalseQuestion("")
        );

        assertThrows(
                NullPointerException.class,
                () -> new TrueOrFalseQuestion(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new TrueOrFalseQuestion(" ")
        );
    }

    @Test
    void should_not_accept_question_mark_on_statement(){
        assertThrows(
                IllegalArgumentException.class,
                () -> new TrueOrFalseQuestion("There is only one solar system?")
        );
    }

    @Test
    void statement_should_end_with_full_stop(){
        assertThrows(
                IllegalArgumentException.class,
                () -> new TrueOrFalseQuestion("There is only one solar system")
        );
    }
}