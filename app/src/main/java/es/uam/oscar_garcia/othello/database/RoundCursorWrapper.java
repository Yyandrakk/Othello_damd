package es.uam.oscar_garcia.othello.database;


import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import es.uam.eps.multij.ExcepcionJuego;
import es.uam.oscar_garcia.othello.model.OthelloBoard;
import es.uam.oscar_garcia.othello.model.Round;
import es.uam.oscar_garcia.othello.model.RoundRepository;

/**
 * Created by oscar on 24/03/17.
 */

public class RoundCursorWrapper extends CursorWrapper {
    private final String DEBUG = "DEBUG";
    private final int size=8;

    public RoundCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Round getRound() {
        String playername = getString(getColumnIndex(RoundDataBaseSchema.UserTable.Cols.PLAYERNAME));
        UUID uuid= UUID.fromString(getString(getColumnIndex(RoundDataBaseSchema.UserTable.Cols.PLAYERUUID)));
        String board=getString(getColumnIndex(RoundDataBaseSchema.RoundTable.Cols.BOARD));
        String date =getString(getColumnIndex(RoundDataBaseSchema.RoundTable.Cols.DATE));
        String id =getString(getColumnIndex(RoundDataBaseSchema.RoundTable.Cols.ROUNDUUID));
        String title=getString(getColumnIndex(RoundDataBaseSchema.RoundTable.Cols.TITLE));
        Round round = new Round(size,uuid,"random");
        round.setFirstPlayerName("random");
        round.setSecondPlayerName(playername);
        round.setDate(date);
        round.setId(id);
        round.setTitle(title);

        try {
            round.getBoard().stringToTablero(board);
            return round;
        } catch (ExcepcionJuego e) {
            Log.d(DEBUG, "Error turning string into tablero");
            return null;
        }

    }


}
