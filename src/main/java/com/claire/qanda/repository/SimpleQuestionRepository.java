package com.claire.qanda.repository;

import com.claire.qanda.model.IdentifiableQuestion;
import com.claire.qanda.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.Collections.unmodifiableList;

/*
where can we call a method in java or a constructor?

1- As a statement in a method or constructor.
2- As initialization on declaration lines (both instance of local variables)
3- As assignment into non-final variables in statements or constructors (if variables were initialized).
 */
public class SimpleQuestionRepository implements QuestionRepository {

    private final List<IdentifiableQuestion> db = new ArrayList<>();

    @Override
    public IdentifiableQuestion save(IdentifiableQuestion question) {
        db.add(question);
        return question;
    }

    @Override
    public List<Question> list() {
        return unmodifiableList(db);
    }

    @Override
    public IdentifiableQuestion getOpenQuestion(Integer id) {
        return db.stream()
                .filter(question -> question.id().equals(id))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public IdentifiableQuestion getOpenQuestion2(Integer id) {
        for (IdentifiableQuestion question : db){
            if (question.id().equals(id)){
                return question;
            }
        }
        throw new NoSuchElementException("cannot find question with id " + id);
    }
}
