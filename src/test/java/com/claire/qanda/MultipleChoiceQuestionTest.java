package com.claire.qanda;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class MultipleChoiceQuestionTest {

    @Test
    void can_create_question_successfully() {
        assertDoesNotThrow(() ->
                new MultipleChoiceQuestion(
                        "Who was the US president during world war I?",
                        asList(
                                "Jack",
                                "John",
                                "Rooseville",
                                "Clinton"
                        )
                )
        );
    }
}