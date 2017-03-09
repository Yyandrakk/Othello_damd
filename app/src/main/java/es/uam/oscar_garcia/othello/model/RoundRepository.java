package es.uam.oscar_garcia.othello.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 1/03/17.
 */

public class RoundRepository {
    public static final int SIZE = 8;
    private static RoundRepository repository;
    private List<Round> rounds;
    public static RoundRepository get(Context context) {
        if (repository == null) {
            repository = new RoundRepository(context);
        }
        return repository;
    }
    private RoundRepository(Context context) {
        rounds = new ArrayList<Round>();
        for (int i = 0; i < 3; i++) {
            Round round = new Round(SIZE);
            rounds.add(round);
        }
    }
    public List<Round> getRounds() {
        return rounds;
    }
    public Round getRound(String id) {
        for (Round round : rounds) {
            if (round.getId().equals(id))
                return round;
        }
        return null;
    }

    public void addRound(Round round){
        rounds.add(round);
    }

    public void deleteRound(Round round){rounds.remove(round);}
}
