package com.claire.qanda;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenQuestionTest {
    @Test
    void cannot_accept_empty_statement() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new OpenQuestion("")
        );
    }

}