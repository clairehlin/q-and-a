package com.claire.qanda;

class TrueOrFalseQuestion {
    private final String statement;
    private final boolean[] selection;

    TrueOrFalseQuestion(String statement) {
        validateStatement(statement);

        this.statement = statement;
        selection = new boolean[]{true, false};
    }

    private void validateStatement(String statement) {
        if (statement.trim().isEmpty()) {
            throw new IllegalArgumentException("the statement cannot be empty");
        }

        if (statement.trim().endsWith("?")) {
            throw new IllegalArgumentException("the statement must not contain question mark");
        }

        if (!statement.trim().endsWith(".")) {
            throw new IllegalArgumentException("the statement must contain full stop mark at the end");
        }
    }

}
