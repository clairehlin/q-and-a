package com.claire.qanda.repository;

import com.claire.qanda.model.IdentifiableQuestion;
import com.claire.qanda.model.Question;

public class QuestionToIdentifiableQuestionAdapter implements IdentifiableQuestion {

    private final Question question;

    public QuestionToIdentifiableQuestionAdapter(Question question) {
        this.question = question;
    }

    @Override
    public Integer id() {
        return null;
    }

    @Override
    public String statement() {
        return question.statement();
    }

    @Override
    public String correctAnswer() {
        return question.correctAnswer();
    }
}
