package com.claire.qanda.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OpenQuestionTest {
    private Integer id = null;

    @Test
    void cannot_accept_empty_statement() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new OpenQuestion(id, "", "fine")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new OpenQuestion(id, "  ", "fine"),
                "statements with only spaces should fail"
        );

        assertThrows(
                NullPointerException.class,
                () -> new OpenQuestion(id, null, "fine")
        );
    }

    @Test
    void can_create_open_question_successfully() {
        assertDoesNotThrow(() -> new OpenQuestion(id, "how are you?", "fine"));
    }

    @Test
    void should_not_accept_empty_answer(){
        assertThrows(
                IllegalArgumentException.class,
                () -> new OpenQuestion(id, "how many leaves in the world?", "")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new OpenQuestion(id, "how many leaves in the world?", " ")
        );

        assertThrows(
                NullPointerException.class,
                () -> new OpenQuestion(id, "how many leaves in the world?", null)
        );
    }
}