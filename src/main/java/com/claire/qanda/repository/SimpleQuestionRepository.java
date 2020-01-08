package com.claire.qanda.repository;

import com.claire.qanda.model.Question;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/*
where can we call a method in java or a constructor?

1- As a statement in a method or constructor.
2- As initialization on declaration lines (both instance of local variables)
3- As assignment into non-final variables in statements or constructors (if variables were initialized).
 */
public class SimpleQuestionRepository implements QuestionRepository {

    private final List<Question> db = new ArrayList<>();

    @Override
    public Question save(Question question) {
        db.add(question);
        return question;
    }

    @Override
    public List<Question> list() {
        return unmodifiableList(db);
    }
}
