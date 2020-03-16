package com.claire.qanda.web;

import com.claire.qanda.services.QuestionsService;
import com.claire.qanda.web.model.WebQuestion;

import java.util.function.BiConsumer;

public class UpdateMultipleChoiceQuestion implements BiConsumer<Integer, WebQuestion> {
    public UpdateMultipleChoiceQuestion(QuestionsService questionsService) {
    }

    @Override
    public void accept(Integer integer, WebQuestion webQuestion) {

    }
}
