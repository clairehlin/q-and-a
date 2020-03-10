package com.claire.qanda.web;

import com.claire.qanda.web.model.WebQuestion;

import java.util.function.Consumer;

public class FailedAddQuestion implements Consumer<WebQuestion> {
    private final String questionType;

    public FailedAddQuestion(String questionType) {
        this.questionType = questionType;
    }

    @Override
    public void accept(WebQuestion webQuestion) {
        throw new IllegalArgumentException("question type is not found: " + questionType);
    }
}
