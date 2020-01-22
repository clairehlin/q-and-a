package com.claire.qanda.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;

public class MultipleChoiceQuestion implements Question {
    private final String initialPhrase;
    private final List<String> choices = new ArrayList<>();
    private final int answer;

    public MultipleChoiceQuestion(String initialPhrase, List<String> choices, int answer) {
        validateInitialPhrase(initialPhrase);
        this.initialPhrase = initialPhrase;

        validateChoices(choices);
        this.choices.addAll(choices);

        validateAnswer(answer);
        this.answer = answer;
    }

    private void validateAnswer(int answer) {
        if (answer < 0) {
            throw new IllegalArgumentException(
                    "answer should be non-negative value, but was: " + answer
            );
        }

        if (answer > choices.size() - 1) {
            throw new IllegalArgumentException(
                    "answer number cannot be greater than the number of options minus one, " +
                            "answer number: " + answer + ", " +
                            "options count: " + choices.size()
            );
        }
    }

    private void validateChoices(List<String> choices) {
        if (choices.size() < 2) {
            throw new IllegalArgumentException("too few choices: " + choices);
        }

        choices.forEach(MultipleChoiceQuestion::failIfEmpty);
    }

    private static void failIfEmpty(String choice) {
        if (choice == null || choice.isEmpty()) {
            throw new IllegalArgumentException("one of the choices is empty");
        }
    }

    private void validateInitialPhrase(String initialPhrase) {
        if (initialPhrase.trim().isEmpty()) {
            throw new IllegalArgumentException("Initial phrase cannot be empty");
        }
    }

    @Override
    public String statement() {
        return initialPhrase + "\n" + choicesAsBullets(choices);
    }

    private String choicesAsBullets(List<String> choices) {
        return IntStream.range(0, choices.size())
                .mapToObj(i -> prefix(i) + choices.get(i))
                .collect(joining("\n"));
    }

    private String choicesAsBullets2(List<String> choices) {
        String combineChoices = "";
        int counter = 1;
        for (String choice : choices) {
            combineChoices = combineChoices + counter + ". " + choice + "\n";
            counter++;
        }
        return combineChoices;
    }

    private String choicesAsBullets3(List<String> choices) {
        StringBuilder combineChoices = new StringBuilder();
        AtomicInteger counter = new AtomicInteger(1);
        choices.forEach(
                choice -> combineChoices
                        .append(counter.getAndIncrement())
                        .append(". ")
                        .append(choice)
                        .append("\n")
        );
        return combineChoices.toString();
    }

    private String choicesAsBullets4(List<String> choices) {
        String combineChoices = "";
        for (int i = 0; i < choices.size(); i++) {
            combineChoices = combineChoices + (i + 1) + ". " + choices.get(i) + "\n";
        }
        return combineChoices;
    }

    private String prefix(int i) {
        return (i + 1) + ". ";
    }

    public String correctAnswer() {
        return choices.get(answer);
    }

    public String getInitialPhrase() {
        return initialPhrase;
    }

    public List<String> getChoices() {
        return unmodifiableList(choices);
    }
}
