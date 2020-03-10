package com.claire.qanda.web;

import com.claire.qanda.model.MultipleChoiceQuestion;
import com.claire.qanda.services.QuestionsService;
import com.claire.qanda.web.model.WebQuestion;

import java.util.function.Consumer;

public class AddMultipleChoiceQuestion implements Consumer<WebQuestion> {
    private final QuestionsService questionsService;

    public AddMultipleChoiceQuestion(QuestionsService questionsService) {
        this.questionsService = questionsService;
    }

    @Override
    public void accept(WebQuestion webQuestion) {
        questionsService.addQuestion(
                new MultipleChoiceQuestion(
                        null,
                        webQuestion.statement,
                        webQuestion.choices,
                        webQuestion.choices.indexOf(webQuestion.answer)
                )
        );
    }
}
