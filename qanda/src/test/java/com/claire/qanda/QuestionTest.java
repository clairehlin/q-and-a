package com.claire.qanda;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    @Test
    void should_not_accept_empty_statement() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Question("", Question.QType.OPEN)
        );
    }

}