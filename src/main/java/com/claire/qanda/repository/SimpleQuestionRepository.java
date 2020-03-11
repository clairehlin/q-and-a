package com.claire.qanda.repository;

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

    @Override
    public Question getQuestion(Integer id) {
        return db.stream()
                .filter(question -> question.id().equals(id))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void deleteQuestionWithId(Integer id) {
        final Question question = db.stream()
                .filter(question1 -> question1.id().equals(id))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        db.remove(question);
    }

    public void deleteQuestionWithId2(Integer id) {
        for (Question question : db) {
            if (question.id().equals(id)){
                db.remove(question);
            }
        }
    }

    @Override
    public void updateQuestion(Question question) {
        for (Question q : db) {
            if (question.id().equals(q.id())){
                db.remove(q);
                db.add(question);
            }
        }
    }

    public Question getOpenQuestion2(Integer id) {
        for (Question question : db) {
            if (question.id().equals(id)) {
                return question;
            }
        }
        throw new NoSuchElementException("cannot find question with id " + id);
    }
}
