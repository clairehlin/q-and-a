package com.claire.qanda;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class OpenQuestionTest {
    @Test
    void cannot_accept_empty_statement() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new OpenQuestion("")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new OpenQuestion("  "),
                "statements with only spaces should fail"
        );

        assertThrows(
                NullPointerException.class,
                () -> new OpenQuestion(null)
        );

    }

    @Test
    void should_accept_nonEmpty_questions() {
        assertDoesNotThrow(() -> new OpenQuestion("how are you?"));
    }


}