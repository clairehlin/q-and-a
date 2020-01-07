package com.claire.qanda.services;

import com.claire.qanda.model.MultipleChoiceQuestion;
import com.claire.qanda.model.OpenQuestion;
import com.claire.qanda.model.Question;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class SimpleQuestionsServiceTest {

    @Test
    void can_add_questions() {
        SimpleQuestionsService simpleQuestionService = new SimpleQuestionsService();
        simpleQuestionService.addQuestion(new OpenQuestion("a", "b"));
        List<Question> questions = simpleQuestionService.list();
        System.out.println(questions);
        questions.add(new MultipleChoiceQuestion("a", asList("ab", "bc"), 0));
        System.out.println(simpleQuestionService.list());
    }

}