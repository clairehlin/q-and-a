package com.claire.qanda;

public class OpenQuestion {
    private final String statement;

    public OpenQuestion(String statement) {
        validateStatementNotEmpty(statement);

        this.statement = statement;
    }

    private void validateStatementNotEmpty(String statement) {
        if (statement.trim().isEmpty()) {
            throw new IllegalArgumentException("cannot accept empty statement");
        }
    }


}
