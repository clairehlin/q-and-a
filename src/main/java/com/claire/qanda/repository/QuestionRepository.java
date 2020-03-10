package com.claire.qanda.repository;

import com.claire.qanda.model.OpenQuestion;
import com.claire.qanda.model.Question;

import java.util.List;

public interface QuestionRepository {
    Question save(Question question);

    List<Question> list();

    void deleteQuestionWithId(Integer id);

    void updateOpenQuestion(OpenQuestion openQuestion);

    Question getQuestion(Integer id);
}
