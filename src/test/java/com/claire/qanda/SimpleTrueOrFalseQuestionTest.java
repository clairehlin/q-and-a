package com.claire.qanda;

class SimpleTrueOrFalseQuestionTest extends AbstractTrueOrFalseQuestionTest {
    @Override
    protected Question trueOrFalseQuestion(String s) {
        return new SimpleTrueOrFalseQuestion(s, true);
    }
}