package es.uam.oscar_garcia.othello.model;

import android.content.Context;

import es.uam.oscar_garcia.othello.actividades.OthelloPreferenceActivity;
import es.uam.oscar_garcia.othello.database.OthelloDataBase;
import es.uam.oscar_garcia.othello.server.ServerRepository;


/**
 * Created by oscar on 22/03/17.
 */

public class RoundRepositoryFactory {

    public static RoundRepository createRepository(Context context) {
        RoundRepository repository;
        boolean online = OthelloPreferenceActivity.getOnline(context);
       // repository = ServerRepository.getInstance(context);
       repository = online ?  ServerRepository.getInstance(context) :  new OthelloDataBase(context);
        try {
            repository.open();
        }
        catch (Exception e) {
            return null;
        }
        return repository;
    }


}
