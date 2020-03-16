package com.claire.qanda.web;

import com.claire.qanda.web.model.WebQuestion;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FailedUpdateQuestion implements BiConsumer<Integer, WebQuestion> {
    private final String questionType;

    public FailedUpdateQuestion(String questionType) {
        this.questionType = questionType;
    }

    @Override
    public void accept(Integer id, WebQuestion webQuestion) {
        throw new IllegalArgumentException("question type is not found: " + questionType);
    }
}
