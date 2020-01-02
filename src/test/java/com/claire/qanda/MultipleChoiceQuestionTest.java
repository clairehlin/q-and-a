package com.claire.qanda;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void should_not_accept_empty_statement() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MultipleChoiceQuestion("",
                        asList(
                                "John",
                                "John",
                                "RooseVille",
                                "Clinton"
                        )
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new MultipleChoiceQuestion(" ",
                        asList(
                                "John",
                                "John",
                                "RooseVille",
                                "Clinton"
                        )
                )
        );

        assertThrows(
                NullPointerException.class,
                () -> new MultipleChoiceQuestion(null,
                        asList(
                                "John",
                                "John",
                                "RooseVille",
                                "Clinton"
                        )
                )
        );
    }

    @Test
    void should_not_accept_less_than_two_choices() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MultipleChoiceQuestion("who is the president during world war I?",
                        singletonList(
                                "John"
                        )
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new MultipleChoiceQuestion("who is the president during world war I?",
                        emptyList()
                )
        );

        assertThrows(
                NullPointerException.class,
                () -> new MultipleChoiceQuestion("who is the president during world war I?",
                        null
                )
        );
    }

    @Test
    void should_reject_empty_choices(){
        assertThrows(
                IllegalArgumentException.class,
                () -> new MultipleChoiceQuestion("who is the president during world war I?",
                        asList(
                                "John",
                                "",
                                "RooseVille",
                                "Clinton"
                        )
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new MultipleChoiceQuestion("who is the president during world war I?",
                        asList(
                                "John",
                                null,
                                "RooseVille",
                                "Clinton"
                        )
                )
        );
    }
}