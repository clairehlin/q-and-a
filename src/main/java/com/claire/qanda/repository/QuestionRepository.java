package com.claire.qanda.repository;

import com.claire.qanda.model.IdentifiableQuestion;
import com.claire.qanda.model.Question;

import java.util.List;

public interface QuestionRepository {
    IdentifiableQuestion save(IdentifiableQuestion question);
    List<Question> list();

    IdentifiableQuestion getOpenQuestion(Integer id);
}
