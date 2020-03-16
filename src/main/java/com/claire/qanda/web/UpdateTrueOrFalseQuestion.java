package com.claire.qanda.web;

import com.claire.qanda.model.SimpleTrueOrFalseQuestion;
import com.claire.qanda.services.QuestionsService;
import com.claire.qanda.web.model.WebQuestion;

import java.util.function.BiConsumer;

public class UpdateTrueOrFalseQuestion implements BiConsumer<Integer, WebQuestion> {
    private final QuestionsService questionsService;

    public UpdateTrueOrFalseQuestion(QuestionsService questionsService) {
        this.questionsService = questionsService;
    }

    @Override
    public void accept(Integer id, WebQuestion webQuestion) {
        questionsService.updateQuestion(
                new SimpleTrueOrFalseQuestion(
                        id,
                        webQuestion.statement,
                        Boolean.parseBoolean(webQuestion.answer)
                )
        );

    }
}
