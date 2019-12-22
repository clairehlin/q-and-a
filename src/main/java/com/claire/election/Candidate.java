package com.claire.election;

class Candidate {
    private final Election election;
    private final int id;
    private final Runnable leaderAction;
    private final Runnable nonLeaderAction;


    Candidate(Election election, int id, Runnable leaderAction, Runnable nonLeaderAction) {
        this.election = election;
        this.id = id;
        this.leaderAction = leaderAction;
        this.nonLeaderAction = nonLeaderAction;
        this.election.expressInterest(this);
    }

    void performDuty() {
        if (election.winner() == this) {
            leaderAction.run();
        } else {
            nonLeaderAction.run();
        }
    }

    int getId() {
        return id;
    }
}
