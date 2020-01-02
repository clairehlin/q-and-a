package com.claire.qanda;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrueOrFalseQuestionTest {

    @Test
    void can_create_question_successfully() {
        assertDoesNotThrow(() ->
                new TrueOrFalseQuestion(
                        "Is Trump the current president?"
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
}