package com.claire.qanda.model;

import static java.util.Arrays.asList;

class InheritanceTrueOrFalseQuestion extends MultipleChoiceQuestion {

    InheritanceTrueOrFalseQuestion(String initialPhrase, boolean answer) {
        super(
                initialPhrase,
                asList(
                        String.valueOf(true),
                        String.valueOf(false)
                ),
                answer ? 0 : 1
        );
        validateInitialPhrase(initialPhrase);
    }

    private void validateInitialPhrase(String initialPhrase) {
        if (initialPhrase.trim().isEmpty()) {
            throw new IllegalArgumentException("the initialPhrase cannot be empty");
        }

        if (initialPhrase.trim().endsWith("?")) {
            throw new IllegalArgumentException("the initialPhrase must not contain question mark");
        }

        if (!initialPhrase.trim().endsWith(".")) {
            throw new IllegalArgumentException("the initialPhrase must contain full stop mark at the end");
        }
    }
}
