package com.claire.election;

class DuplicateCandidateIdException extends IllegalArgumentException {
    DuplicateCandidateIdException(String failureMessage) {
        super(failureMessage);
    }
}
