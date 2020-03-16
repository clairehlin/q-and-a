package com.claire.qanda.web;

import com.claire.qanda.model.MultipleChoiceQuestion;
import com.claire.qanda.services.QuestionsService;
import com.claire.qanda.web.model.WebQuestion;

import java.util.function.BiConsumer;

public class UpdateMultipleChoiceQuestion implements BiConsumer<Integer, WebQuestion> {
    private final QuestionsService questionsService;

    public UpdateMultipleChoiceQuestion(QuestionsService questionsService) {
        this.questionsService = questionsService;
    }

    @Override
    public void accept(Integer id, WebQuestion webQuestion) {
        questionsService.updateQuestion(
                new MultipleChoiceQuestion(
                        id,
                        webQuestion.statement,
                        webQuestion.choices,
                        webQuestion.choices.indexOf(webQuestion.answer)
                )
        );

    }
}
