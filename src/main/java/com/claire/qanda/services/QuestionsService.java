package com.claire.qanda.services;

import com.claire.qanda.model.Question;
import com.claire.qanda.model.OpenQuestion;
import com.claire.qanda.model.Question;

import java.util.List;

public interface QuestionsService {
    void addQuestion(Question question);

    List<Question> list();

    Question get(Integer id);

    void deleteQuestionWithId(Integer id);

    void updateOpenQuestion(OpenQuestion openQuestion);

    void updateQuestion(Question question);
}
