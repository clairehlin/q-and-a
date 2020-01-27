package com.claire.qanda.services;

import com.claire.qanda.model.Question;

import java.util.List;

public interface QuestionsService {
    void addQuestion(Question question);

    List<Question> list();
}
