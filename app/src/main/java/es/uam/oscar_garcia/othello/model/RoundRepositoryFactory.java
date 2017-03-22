package es.uam.oscar_garcia.othello.model;

import android.content.Context;

import es.uam.oscar_garcia.othello.database.OthelloDataBase;

/**
 * Created by oscar on 22/03/17.
 */

public class RoundRepositoryFactory {
    private static final boolean LOCAL = true;
    public static RoundRepository createRepository(Context context) {
        RoundRepository repository;
        repository = LOCAL ? new OthelloDataBase(context) : new OthelloDataBase(context);
        try {
            repository.open();
        }
        catch (Exception e) {
            return null;
        }
        return repository;
    }


}
