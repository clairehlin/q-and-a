package com.claire.qanda.model;

import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

public class SimpleTrueOrFalseQuestion implements Question {
    private final String initialPhrase;
    private final boolean[] choices;
    private final boolean answer;

    public SimpleTrueOrFalseQuestion(String initialPhrase, boolean answer) {
        this.answer = answer;
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

    @Override
    public String statement() {
        return initialPhrase + "\n" + choicesAsBullets(choices);
    }

    public String getInitialPhrase() {
        return initialPhrase;
    }

    private String choicesAsBullets2(boolean[] choices) {
        String combineChoices = "";
        int counter = 1;
        for (boolean choice : choices) {
            combineChoices = combineChoices + counter + ". " + choice + "\n";
            counter++;
        }
        return combineChoices;
    }

    private String choicesAsBullets(boolean[] choices) {
        return IntStream.range(0, choices.length)
                .mapToObj(i -> prefix(i) + choices[i])
                .collect(joining("\n"));
    }

    private String prefix(int i) {
        return (i + 1) + ". ";
    }


    @Override
    public String correctAnswer() {
        return String.valueOf(answer);
    }
}
