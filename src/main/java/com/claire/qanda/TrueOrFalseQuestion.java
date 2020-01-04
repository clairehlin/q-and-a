package com.claire.qanda;

class TrueOrFalseQuestion {
    private final String initialPhrase;
    private final boolean[] choices;

    TrueOrFalseQuestion(String initialPhrase) {
        validateInitialPhrase(initialPhrase);

        this.initialPhrase = initialPhrase;
        choices = new boolean[]{true, false};
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
