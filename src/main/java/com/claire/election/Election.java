package com.claire.election;

import java.util.HashSet;
import java.util.Set;

import static java.util.Comparator.comparingInt;

class Election {
    private static final Candidate NO_LEADER = null;
    private final Set<Candidate> candidates = new HashSet<>();

    void expressInterest(Candidate candidate) {
        candidates.add(candidate);
    }

    Candidate winner() {
        return candidates.stream()
                .max(comparingInt(Candidate::getId))
                .orElse(NO_LEADER);
    }
}
