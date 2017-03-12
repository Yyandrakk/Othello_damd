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

    /**
     * Singleton del contenedor de rondas
     * @param context
     * @return
     */
    public static RoundRepository get(Context context) {
        if (repository == null) {
            repository = new RoundRepository(context);
        }
        return repository;
    }

    /**
     * Añade 3 rondas por defecto a la lista de rondas
     * @param context
     */
    private RoundRepository(Context context) {
        rounds = new ArrayList<Round>();
        for (int i = 0; i < 3; i++) {
            Round round = new Round(SIZE);
            rounds.add(round);
        }
    }

    /**
     *
     * @return lista de rondas
     */
    public List<Round> getRounds() {
        return rounds;
    }

    /**
     * Devuelve una ronda de la lista segun su id
     * @param id
     * @return
     */
    public Round getRound(String id) {
        for (Round round : rounds) {
            if (round.getId().equals(id))
                return round;
        }
        return null;
    }

    /**
     * anyade la nueva ronda
     * @param round
     */
    public void addRound(Round round){
        rounds.add(round);
    }


    /**
     * Borra una ronda
     * @param round
     */
    public void deleteRound(Round round){rounds.remove(round);}
}
