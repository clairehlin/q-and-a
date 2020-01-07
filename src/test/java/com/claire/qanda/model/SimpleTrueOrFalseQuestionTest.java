package com.claire.qanda.model;

class SimpleTrueOrFalseQuestionTest extends AbstractTrueOrFalseQuestionTest {
    @Override
    protected Question trueOrFalseQuestion(String s) {
        return new SimpleTrueOrFalseQuestion(s, true);
    }
}