package com.claire.qanda.services;

import com.claire.qanda.model.IdentifiableQuestion;
import com.claire.qanda.model.OpenQuestion;
import com.claire.qanda.model.Question;

import java.util.List;

public interface QuestionsService {
    void addQuestion(IdentifiableQuestion question);

    List<Question> list();

    Question get(Integer id);

    void deleteQuestionWithId(Integer id);

    void updateOpenQuestion(OpenQuestion openQuestion);
}
