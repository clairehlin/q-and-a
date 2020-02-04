package com.claire.qanda.repository;

class DatabaseException extends RuntimeException {
    DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
