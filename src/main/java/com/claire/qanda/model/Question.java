package com.claire.qanda.model;

public interface Question extends IntegerIdentifiable {
    String statement();
    String correctAnswer();
}
