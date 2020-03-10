package com.claire.qanda.web;

import com.claire.qanda.model.SimpleTrueOrFalseQuestion;
import com.claire.qanda.services.QuestionsService;
import com.claire.qanda.web.model.WebQuestion;

import java.util.function.Consumer;

import static java.lang.Boolean.parseBoolean;

public class AddTrueOrFalseQuestion implements Consumer<WebQuestion> {
    private final QuestionsService questionsService;

    public AddTrueOrFalseQuestion(QuestionsService questionsService) {
        this.questionsService = questionsService;
    }

    @Override
    public void accept(WebQuestion webQuestion) {
        questionsService.addQuestion(
                new SimpleTrueOrFalseQuestion(
                        null,
                        webQuestion.statement,
                        parseBoolean(webQuestion.answer)
                )
        );
    }
}
