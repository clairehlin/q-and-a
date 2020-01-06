package com.claire.qanda;

class SimpleTrueOrFalseQuestionTest extends AbstractTrueOrFalseQuestionTest {
    @Override
    protected Question trueOrFalseQuestion(String s) {
        return new SimpleTrueOrFalseQuestion(s, true);
//        return new CompositionTrueOrFalseQuestion(s, true);
//        return new InheritanceTrueOrFalseQuestion(s, true);
    }
}