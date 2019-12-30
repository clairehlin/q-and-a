package com.claire.qanda;

import java.util.ArrayList;
import java.util.List;

class MultipleChoiceQuestion {
    private final String statement;
    private final List<String> choiceStatements = new ArrayList<>();

    MultipleChoiceQuestion(String statement, List<String> choiceStatements) {
        this.statement = statement;
        this.choiceStatements.addAll(choiceStatements);
    }
}
