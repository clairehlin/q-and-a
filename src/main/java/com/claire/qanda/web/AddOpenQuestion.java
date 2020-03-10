package com.claire.qanda.web;

import com.claire.qanda.model.OpenQuestion;
import com.claire.qanda.services.QuestionsService;
import com.claire.qanda.web.model.WebQuestion;

import java.util.function.Consumer;

public class AddOpenQuestion implements Consumer<WebQuestion> {
    private final QuestionsService questionsService;

    public AddOpenQuestion(QuestionsService questionsService) {
        this.questionsService = questionsService;
    }

    @Override
    public void accept(WebQuestion webQuestion) {
        questionsService.addQuestion(
                new OpenQuestion(null, webQuestion.statement, webQuestion.answer)
        );
    }
}
