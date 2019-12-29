package com.claire.election;

import java.util.*;
import java.util.function.Predicate;

import static java.util.Comparator.comparingInt;
import static java.util.concurrent.TimeUnit.SECONDS;

class Election {

    private final Set<Candidate> candidates = new HashSet<>();

    Election() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(
                new CandidateCleanUpTask(),
                0,
                SECONDS.toMillis(5)
        );
    }

    void expressInterest(Candidate candidate) {
        checkNoCandidateWithId(candidate.getId());
        candidates.add(candidate);
    }

    private void checkNoCandidateWithId(int id) {
        Predicate<Candidate> hasSameId = candidate -> candidate.getId() == id;
        boolean alreadyExists = candidates.stream()
                .anyMatch(hasSameId);
        if(alreadyExists){
            throw new DuplicateCandidateIdException("a candidate already exists with id " + id);
        }
    }

    Candidate winner() {
        return candidates.stream()
                .max(comparingInt(Candidate::getId))
                .orElseThrow(NoCandidateFoundException::new);
    }

    private class CandidateCleanUpTask extends TimerTask {
        @Override
        public void run() {
            new ArrayList<>(candidates).stream()
                    .filter(Candidate::isNotAlive)
                    .forEach(candidates::remove);
        }
    }
}
