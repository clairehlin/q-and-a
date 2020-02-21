package com.claire.qanda.model;

import static java.util.Arrays.asList;

class CompositionTrueOrFalseQuestion implements Question {
    private final MultipleChoiceQuestion multipleChoiceQuestion;

    CompositionTrueOrFalseQuestion(String initialPhrase, boolean answer) {
        validateInitialPhrase(initialPhrase);
        multipleChoiceQuestion = new MultipleChoiceQuestion(
                initialPhrase,
                asList(
                        String.valueOf(true),
                        String.valueOf(false)
                ),
                answer ? 0 : 1
        );
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

    @Override
    public String statement() {
        return multipleChoiceQuestion.statement();
    }

    @Override
    public String correctAnswer() {
        return multipleChoiceQuestion.correctAnswer();
    }

    @Override
    public Integer id() {
        return multipleChoiceQuestion.id();
    }



}
