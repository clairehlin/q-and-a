package com.claire.qanda.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class H2QuestionRepositoryTest {

    @Test
    void can_connect_to_db() {
        H2QuestionRepository h2QuestionRepository = new H2QuestionRepository();
        h2QuestionRepository.list();
    }
}