package com.claire.election;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CandidateElectionTest {
    @Test
    void election_should_result_in_highest_id_candidate_winning(){
        // given
        List<String> actionTaken = new ArrayList<>();
        Runnable leaderAction = () -> actionTaken.add("leader action");
        Runnable nonLeaderAction = () -> actionTaken.add("nonLeader action");

        Election election = new Election();
        Candidate c1 = new Candidate(election, 1, leaderAction, nonLeaderAction);
        Candidate c2 = new Candidate(election, 2, leaderAction, nonLeaderAction);
        Candidate c3 = new Candidate(election, 3, leaderAction, nonLeaderAction);

        // when
        Candidate leader = election.winner();

        // then
        assertEquals(c3, leader);

        // when
        c3.performDuty();

        // then
        assertEquals(actionTaken, singletonList("leader action"));

        // when
        actionTaken.clear();
        c2.performDuty();

        // then
        assertEquals(actionTaken, singletonList("nonLeader action"));

        // when
        actionTaken.clear();
        c1.performDuty();

        // then
        assertEquals(actionTaken, singletonList("nonLeader action"));
    }

}