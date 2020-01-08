package com.claire.qanda.services;

import com.claire.qanda.model.Question;
import com.claire.qanda.repository.QuestionRepository;
import com.claire.qanda.repository.SimpleQuestionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.*;

public class SimpleQuestionsService implements QuestionsService {

//    private final List<Question> db = new ArrayList<>();
    private final QuestionRepository db = new SimpleQuestionRepository();

    @Override
    public void addQuestion(Question question) {
        requireNonNull(question, "cannot process null question");
        db.save(question);
    }

    public List<Question> list() {
        return unmodifiableList(db.list());
    }
}
