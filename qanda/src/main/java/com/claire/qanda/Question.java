package com.claire.qanda;

public class Question {

    private final String statement;
    private final QType type;

    public Question(String statement, QType type) {
        if (statement.trim().isEmpty()) {
            throw new IllegalArgumentException("cannot accept empty Question statement");
        }
        if(type == null) {
            throw new NullPointerException("cannot initialize question without a type");
        }
        this.statement = statement;
        this.type = type;
    }

    enum QType {
        MULTIPLE_CHOICE,
        OPEN,
        TRUE_OR_FALSE
    }

}
