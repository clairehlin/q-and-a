package com.claire.qanda.repository;

import com.claire.qanda.model.Question;

import java.util.List;

public interface QuestionRepository {
    Question save(Question question);

    List<Question> list();

    void deleteQuestionWithId(Integer id);

    Question getQuestion(Integer id);

    void updateQuestion(Question question);
}
