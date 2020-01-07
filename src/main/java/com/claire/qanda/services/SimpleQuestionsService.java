package com.claire.qanda.services;

import com.claire.qanda.model.Question;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class SimpleQuestionsService implements QuestionsService {

    private final List<Question> db = new ArrayList<>();

    @Override
    public void addQuestion(Question question) {
        db.add(question);
    }

    public List<Question> list() {
        return unmodifiableList(db);
    }
}
