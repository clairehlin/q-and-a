package com.claire.qanda.model;

class SimpleTrueOrFalseQuestionTest extends AbstractTrueOrFalseQuestionTest {
    private final Integer id = null;

    @Override
    protected Question trueOrFalseQuestion(String s) {
        return new SimpleTrueOrFalseQuestion(id, s, true);
    }
}