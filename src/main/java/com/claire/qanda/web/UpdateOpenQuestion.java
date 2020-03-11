package com.claire.qanda.web;

import com.claire.qanda.model.OpenQuestion;
import com.claire.qanda.services.QuestionsService;
import com.claire.qanda.web.model.WebQuestion;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class UpdateOpenQuestion implements BiConsumer<Integer, WebQuestion> {
    private final QuestionsService questionsService;

    public UpdateOpenQuestion(QuestionsService questionsService) {
        this.questionsService = questionsService;
    }

    @Override
    public void accept(Integer id, WebQuestion webQuestion) {
        questionsService.updateQuestion(
                new OpenQuestion(id, webQuestion.statement, webQuestion.answer)
        );

    }
}
