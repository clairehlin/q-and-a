package com.claire.qanda;

public class Question {

    private final String statement;
    private final QType type;

    public Question(String statement, QType type) throws Exception {
        if (statement.trim().isEmpty()) {
            throw new IllegalArgumentException("cannot accept empty Question statement");
        }
        if(type == null) {
            throw new NullPointerException("cannot initialize question without a type");
        }

        if (statement.length() > 5) {
            throw new IllegalArgumentException("Length of statement is longer than 5 ");
        } else {
            System.out.println("length is shorter than 5");
        }

        this.statement = statement;
        this.type = type;
    }

    enum QType {
        MULTIPLE_CHOICE,
        OPEN,
        TRUE_OR_FALSE
    }

    public String getStatement() {
        return statement;
    }

    public QType getQType(){
        return type;
    }
}