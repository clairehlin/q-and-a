package com.claire.qanda;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenQuestionTest {
    @Test
    void cannot_accept_empty_statement() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new OpenQuestion("", "fine")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new OpenQuestion("  ", "fine"),
                "statements with only spaces should fail"
        );

        assertThrows(
                NullPointerException.class,
                () -> new OpenQuestion(null, "fine")
        );
    }

    @Test
    void can_create_open_question_successfully() {
        assertDoesNotThrow(() -> new OpenQuestion("how are you?", "fine"));
    }

    @Test
    void should_not_accept_empty_answer(){
        assertThrows(
                IllegalArgumentException.class,
                () -> new OpenQuestion("how many leaves in the world?", "")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new OpenQuestion("how many leaves in the world?", " ")
        );

        assertThrows(
                NullPointerException.class,
                () -> new OpenQuestion("how many leaves in the world?", null)
        );
    }
}