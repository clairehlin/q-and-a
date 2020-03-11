package com.claire.qanda.services;

import com.claire.qanda.model.Question;
import com.claire.qanda.model.OpenQuestion;
import com.claire.qanda.model.Question;
import com.claire.qanda.repository.QuestionRepository;

import java.util.List;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.*;

public class SimpleQuestionsService implements QuestionsService {

    private final QuestionRepository db;

    public SimpleQuestionsService(QuestionRepository db) {
        this.db = db;
    }

    @Override
    public void addQuestion(Question question) {
        requireNonNull(question, "cannot process null question");
        db.save(question);
    }

    @Override
    public List<Question> list() {
        return unmodifiableList(db.list());
    }

    @Override
    public Question get(Integer id) {
        return db.getQuestion(id);
    }

    @Override
    public void deleteQuestionWithId(Integer id) {
        db.deleteQuestionWithId(id);
    }

    @Override
    public void updateOpenQuestion(OpenQuestion openQuestion) {
        db.updateOpenQuestion(openQuestion);
    }

    @Override
    public void updateQuestion(Question question) {
        db.updateQuestion(question);
    }
}
