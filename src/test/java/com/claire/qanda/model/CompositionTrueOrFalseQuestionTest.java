package com.claire.qanda.model;

class CompositionTrueOrFalseQuestionTest extends AbstractTrueOrFalseQuestionTest {
    @Override
    protected Question trueOrFalseQuestion(String s) {
        return new CompositionTrueOrFalseQuestion(s, true);
    }
}