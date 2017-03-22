package es.uam.oscar_garcia.othello.model;

import logica_juego.TableroOthello;

/**
 * Created by oscar on 1/03/17.
 */

public class OthelloBoard extends TableroOthello {
    final private int size;
    public static final int JUGADOR1 = 1;
    public static final int JUGADOR2 = 2;

    /**
     * Constructor del tablero
     * @param size tamano del tablero
     */
    public OthelloBoard(int size){
       super();
       this.size=size;
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

    /**
     * Devuelve el numero fichas del Jugador 1
     * @return int
     */
    public int getNumFichasJ1(){
        int tablero[][]=super.getTab();
        int sum=0,i,j;

        for(i=0;i<size;i++){
            for(j=0;j<size;j++){
                if(tablero[i][j]==JUGADOR1){
                   sum++;
                }
            }
        }

        return sum;
    }

    /**
     * Devuelve el numero fichas del Jugador 2
     * @return int 
     */
    public int getNumFichasJ2(){
        int tablero[][]=super.getTab();
        int sum=0,i,j;

        for(i=0;i<size;i++){
            for(j=0;j<size;j++){
                if(tablero[i][j]==JUGADOR2){
                    sum++;
                }
            }
        }

        return sum;
    }
}
