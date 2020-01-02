package com.claire.qanda;

import java.util.ArrayList;
import java.util.List;

class MultipleChoiceQuestion {
    private final String statement;
    private final List<String> choiceStatements = new ArrayList<>();

    MultipleChoiceQuestion(String statement, List<String> choiceStatements) {
        validateStatement(statement);
        validateChoiceStatements(choiceStatements);

        this.statement = statement;
        this.choiceStatements.addAll(choiceStatements);
    }

    private void validateChoiceStatements(List<String> choiceStatements) {
        if (choiceStatements.size() < 2) {
            throw new IllegalArgumentException("too few choices: " + choiceStatements);
        }

        choiceStatements.forEach(MultipleChoiceQuestion::failIfEmpty);
    }

    private static void failIfEmpty(String choice) {
        if (choice == null || choice.isEmpty()){
            throw new IllegalArgumentException("one of the choices is empty");
        }
    }

    private void validateStatement(String statement) {
        if (statement.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
