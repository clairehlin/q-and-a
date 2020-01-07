package com.claire.qanda.model;

import com.claire.qanda.model.MultipleChoiceQuestion;
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
                        ),
                        3
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
                        ),
                        3
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
                        ),
                        3
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
                        ),
                        3
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
                        ),
                        3
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new MultipleChoiceQuestion("who is the president during world war I?",
                        emptyList(),
                        3
                )
        );

        assertThrows(
                NullPointerException.class,
                () -> new MultipleChoiceQuestion("who is the president during world war I?",
                        null,
                        3
                )
        );
    }

    @Test
    void should_reject_empty_choices() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MultipleChoiceQuestion("who is the president during world war I?",
                        asList(
                                "John",
                                "",
                                "RooseVille",
                                "Clinton"
                        ),
                        3
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
                        ),
                        3
                )
        );
    }

    @Test
    void reject_negative_number_of_answer() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MultipleChoiceQu estion(
                        "Who was the US president during world war I?",
                        asList(
                                "Jack",
                                "John",
                                "Rooseville",
                                "Clinton"
                        ),
                        -1
                )
        );
    }

    @Test
    void reject_answer_outside_the_options_index_range() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MultipleChoiceQuestion(
                        "Who was the US president during world war I?",
                        asList(
                                "Jack",
                                "John",
                                "Rooseville",
                                "Clinton"
                        ),
                        4
                )
        );
    }
}