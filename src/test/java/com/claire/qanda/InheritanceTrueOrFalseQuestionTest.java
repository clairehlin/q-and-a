package com.claire.qanda;

class InheritanceTrueOrFalseQuestionTest extends AbstractTrueOrFalseQuestionTest {
    @Override
    protected Question trueOrFalseQuestion(String s) {
        return new InheritanceTrueOrFalseQuestion(s, true);
    }
}