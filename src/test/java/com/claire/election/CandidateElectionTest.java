package com.claire.election;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CandidateElectionTest {
    @Test
    void election_should_result_in_highest_id_candidate_winning() {
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

    @Test
    void should_remove_not_alive_candidate_after_5_seconds_from_election() throws InterruptedException {
        // given
        Runnable r = () -> {
        };
        Election election = new Election();
        new Candidate(election, 1, r, r);
        Candidate c2 = new Candidate(election, 2, r, r);
        Candidate c3 = new Candidate(election, 3, r, r) {
            @Override
            boolean isNotAlive() {
                return true;
            }
        };

        // when
        Candidate winner = election.winner();

        // then
        assertEquals(c3, winner);

        // when
        Thread.sleep(5500);
        Candidate winnerAfter5 = election.winner();

        // then
        assertEquals(c2, winnerAfter5);
    }

    @Test
    void should_not_allow_two_candidate_with_same_id_to_join_election() {
        // given
        Runnable r = () -> {
        };
        Election election = new Election();
        new Candidate(election, 1, r, r);

        // when/then
        assertThrows(
                DuplicateCandidateIdException.class,
                () -> new Candidate(election, 1, r, r)
        );
    }

    @Test
    void should_fail_to_choose_a_winner_when_there_are_no_candidates() {
        // given
        Election election = new Election();

        // when/then
        assertThrows(
                NoCandidateFoundException.class,
                election::winner
        );
    }
}