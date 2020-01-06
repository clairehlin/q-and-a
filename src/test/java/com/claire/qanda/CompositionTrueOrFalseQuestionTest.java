package com.claire.qanda;

class CompositionTrueOrFalseQuestionTest extends AbstractTrueOrFalseQuestionTest {
    @Override
    protected Question trueOrFalseQuestion(String s) {
        return new CompositionTrueOrFalseQuestion(s, true);
    }
}