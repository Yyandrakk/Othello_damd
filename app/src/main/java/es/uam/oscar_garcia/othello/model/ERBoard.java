package es.uam.oscar_garcia.othello.model;

import logica_juego.TableroOthello;

/**
 * Created by oscar on 1/03/17.
 */

public class ERBoard extends TableroOthello {
    public static final int JUGADOR1 = 1;

    public ERBoard(int size){
        super();
    }

    public int getTablero(int i, int j) {
        return super.getTab()[i][j];
    }
}
