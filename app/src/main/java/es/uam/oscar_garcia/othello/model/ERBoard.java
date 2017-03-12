package es.uam.oscar_garcia.othello.model;

import logica_juego.TableroOthello;

/**
 * Created by oscar on 1/03/17.
 */

public class ERBoard extends TableroOthello {
    public static final int JUGADOR1 = 1;
    public static final int JUGADOR2 = 2;

    /**
     * Constructor del tablero
     * @param size
     */
    public ERBoard(int size){
        super();
    }

    /**
     * Devuelve el valor que tenga el tablero en esa posicion
     * @param i fila
     * @param j columna
     * @return valor entre 0-2
     */
    public int getTablero(int i, int j) {
        return super.getTab()[i][j];
    }
}
