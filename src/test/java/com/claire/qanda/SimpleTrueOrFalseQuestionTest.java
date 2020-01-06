package com.claire.qanda;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TrueOrFalseQuestionTest {

    @Test
    void can_create_question_successfully() {
        assertDoesNotThrow(() ->
                trueOrFalseQuestion("Trump is the current president.")
        );
    }

    @Test
    void should_not_accept_empty_statement() {
        assertThrows(
                IllegalArgumentException.class,
                () -> trueOrFalseQuestion("")
        );

        assertThrows(
                NullPointerException.class,
                () -> trueOrFalseQuestion(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> trueOrFalseQuestion(" ")
        );
    }

    @Test
    void should_not_accept_question_mark_on_statement() {
        assertThrows(
                IllegalArgumentException.class,
                () -> trueOrFalseQuestion("There is only one solar system?")
        );
    }

    @Test
    void statement_should_end_with_full_stop() {
        assertThrows(
                IllegalArgumentException.class,
                () -> trueOrFalseQuestion("There is only one solar system")
        );
    }

    private TrueOrFalseQuestion trueOrFalseQuestion(String s) {
        return new TrueOrFalseQuestion(s, true);
    }

    @Test
    void how_to_print_array_elements(){
        int[] a = new int[]{1,2,3};
        System.out.println(String.valueOf(a[1]));
    }
}