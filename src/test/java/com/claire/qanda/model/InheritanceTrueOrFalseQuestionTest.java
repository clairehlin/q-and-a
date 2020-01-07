package com.claire.qanda.model;

class InheritanceTrueOrFalseQuestionTest extends AbstractTrueOrFalseQuestionTest {
    @Override
    protected Question trueOrFalseQuestion(String s) {
        return new InheritanceTrueOrFalseQuestion(s, true);
    }
}