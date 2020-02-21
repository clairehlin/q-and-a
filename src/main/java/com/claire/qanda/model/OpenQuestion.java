package com.claire.qanda.model;

public class OpenQuestion implements Question {
    private final String statement;
    private final String answer;
    private final Integer id;

    public OpenQuestion(Integer id, String statement, String answer) {
        validateStatementNotEmpty(statement);
        validateAnswerNotEmpty(answer);

        this.id = id;
        this.statement = statement;
        this.answer = answer;
    }

    private void validateAnswerNotEmpty(String answer) {
        if (answer.trim().isEmpty()) {
            throw new IllegalArgumentException("answer should not be empty");
        }
    }

    private void validateStatementNotEmpty(String statement) {
        if (statement.trim().isEmpty()) {
            throw new IllegalArgumentException("cannot accept empty statement");
        }
    }

    @Override
    public String statement() {
        return statement;
    }

    @Override
    public String correctAnswer() {
        return answer;
    }

    @Override
    public Integer id() {
        return id;
    }

    public OpenQuestion withId(Integer id){
        return new OpenQuestion(id, this.statement, this.answer);
    }
}
