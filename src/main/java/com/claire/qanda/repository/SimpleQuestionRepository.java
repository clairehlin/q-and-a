package com.claire.qanda.repository;

import com.claire.qanda.model.MultipleChoiceQuestion;
import com.claire.qanda.model.OpenQuestion;
import com.claire.qanda.model.Question;
import com.claire.qanda.model.SimpleTrueOrFalseQuestion;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

/*
where can we call a method in java or a constructor?

1- As a statement in a method or constructor.
2- As initialization on declaration lines (both instance of local variables)
3- As assignment into non-final variables in statements or constructors (if variables were initialized).
 */
public class SimpleQuestionRepository implements QuestionRepository {

    public static final List<Class<? extends Question>> KNOWN_QUESTION_TYPES = asList(
            OpenQuestion.class,
            SimpleTrueOrFalseQuestion.class,
            MultipleChoiceQuestion.class
    );
    private final List<Question> db = new ArrayList<>();

    @Override
    public Question save(Question question) {
        Question questionWithId = withId(question);
        db.add(questionWithId);
        return questionWithId;
    }

    private Question withId(Question question) {
        Integer id = getMaxId() + 1;
        return withId(question, id);
    }

    private Question withId(Question question, Integer id) {
        if (question.getClass() == OpenQuestion.class) {
            return ((OpenQuestion) question).withId(id);
        } else if (question.getClass() == SimpleTrueOrFalseQuestion.class) {
            return ((SimpleTrueOrFalseQuestion) question).withId(id);
        } else if (question.getClass() == MultipleChoiceQuestion.class) {
            return ((MultipleChoiceQuestion) question).withId(id);
        } else {
            throw new IllegalArgumentException("unknown question type " + question.getClass().getName());
        }
    }

    private Integer getMaxId() {
        return db.stream()
                .map(Question::id)
                .max(Integer::compareTo)
                .orElse(0);
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

    @Override
    public void updateQuestion(Question question) {
        checkQuestionHasValidType(question);
       if (
               db.removeIf(
                       q -> q.id().equals(question.id())
               )
       ) {
           db.add(question);
       } else {
           throw new NoSuchElementException("could not find question with id " + question.id());
       }
    }

    private void checkQuestionHasValidType(Question question) {
       if (!KNOWN_QUESTION_TYPES.contains(question.getClass())) {
           throw new IllegalArgumentException("could not process question of type " + question.getClass().getName());
       }
    }
}
