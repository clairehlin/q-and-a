package com.claire.qanda.services;

import com.claire.qanda.model.OpenQuestion;
import com.claire.qanda.model.Question;
import com.claire.qanda.repository.SimpleQuestionRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleQuestionsServiceTest {

    @Test
    void can_add_questions() {
        SimpleQuestionsService simpleQuestionService = new SimpleQuestionsService(new SimpleQuestionRepository());
        simpleQuestionService.addQuestion(new OpenQuestion(null, "a", "b"));
        List<Question> questions = simpleQuestionService.list();
        assertEquals(1, questions.size());
    }
}