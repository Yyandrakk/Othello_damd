package es.uam.oscar_garcia.othello.model;



import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Tablero;

/**
 * Created by oscar on 1/03/17.
 */

public class OthelloBoard extends Tablero {
    final private int size;
    public static final int JUGADOR1 = 1;
    public static final int JUGADOR2 = 2;
    private int[][] tab;
    final private String ID_ESTADO = "ID_ESTADO";
    final private String ID_TAB = "ID_TAB";
    final private String ID_TURNO = "ID_TURNO";
    final private String ID_NUM_JUG = "ID_NUM_JUG";
    final private String ID_JUGADORES = "ID_JUGADORES";


    public int[][] getTab() {
        return tab;
    }


    public void setTab(int[][] tab) {
        this.tab = tab;
    }




    @Override
    protected void mueve(Movimiento m) throws ExcepcionJuego {

        if (m == null) {
            throw new ExcepcionJuego("Movimiento es null");
        }

        if ((m instanceof MovimientoPasar)) {
            puedenJugar();
            return;
        } else if (esValido(m)) {
            MovimientoOthello mov = (MovimientoOthello) m;
            tab[mov.fila][mov.columna] = (getTurno() == 0) ? 1 : 2;
            ultimoMovimiento = mov;
            cambiarTablero(mov);
            puedenJugar();

        } else {
            throw new ExcepcionJuego("Invalido");
        }

    }
    /**
     * Comprueba si se ha terminado la partida o tiene que pasar turno
     */
    private void puedenJugar() {

        boolean jP = (movimientosValidos().get(0)) instanceof MovimientoPasar;
        cambiaTurno();
        boolean jS = (movimientosValidos().get(0)) instanceof MovimientoPasar;
        if (jP && jS) {

            comprobarEstado();
        } else if (!jP && jS) {
            cambiaTurno();
        }

    }
    /**
     * Comprueba como ha terminado la partida
     * En tablas o finalizada
     */
    private void comprobarEstado() {
        int actual = 0, otro = 0, jug, i, j;
        jug=(getTurno()==0)?1:2;
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                if (tab[i][j] == 0) {

                } else if (tab[i][j] == jug) {
                    actual++;
                } else {
                    otro++;
                }
            }
        }

        if (actual == otro) {

            estado = Tablero.TABLAS;
        } else if (actual > otro) {

            estado = Tablero.FINALIZADA;
        } else {

            cambiaTurno();
            estado = Tablero.FINALIZADA;
        }

    }
    /**
     * Actualiza el tablero cambiando de color las fichas
     * @param mov - movimiento que ha sido validado
     */
    private void cambiarTablero(MovimientoOthello mov) {
        int i, j, jug = (getTurno() == 0) ? 1 : 2;
        boolean status = false;

        // Comprobar Horizontal izq
        for (j = mov.columna - 1; j >= 0; j--) {
            if (tab[mov.fila][j] == 0) {
                break;
            }
            if (tab[mov.fila][j] == jug) {
                status = true;
                break;
            }
        }
        if (status) {
            for (j++; j != mov.columna; j++) {
                tab[mov.fila][j] = jug;
            }
        }
        status = false;
        // Comprobar Horizontal der
        for (j = mov.columna + 1; j < 8; j++) {
            if (tab[mov.fila][j] == 0) {
                break;
            }
            if (tab[mov.fila][j] == jug) {
                status = true;
                break;
            }
        }
        if (status) {
            for (j--; j != mov.columna; j--) {
                tab[mov.fila][j] = jug;
            }
        }
        status = false;
        // Comprobar Vertical Arriba
        for (i = mov.fila - 1; i >= 0; i--) {
            if (tab[i][mov.columna] == 0) {
                break;
            }
            if (tab[i][mov.columna] == jug) {
                status = true;
                break;
            }
        }
        if (status) {
            for (i++; i != mov.fila; i++) {
                tab[i][mov.columna] = jug;
            }
        }
        status = false;
        // Comprobar Vertical Abajo
        for (i = mov.fila + 1; i < 8; i++) {
            if (tab[i][mov.columna] == 0) {
                break;
            }
            if (tab[i][mov.columna] == jug) {
                status = true;
                break;
            }
        }
        if (status) {
            for (i--; i != mov.fila; i--) {
                tab[i][mov.columna] = jug;
            }
        }
        status = false;
        // Comprobar Diagonal izq Abajo
        for (i = mov.fila + 1, j = mov.columna + 1; i < 8 && j < 8; i++, j++) {

            if (tab[i][j] == 0) {
                break;
            }
            if (tab[i][j] == jug) {
                status = true;
                break;
            }
        }
        if (status) {
            for (i--, j--; i != mov.fila; i--, j--) {
                tab[i][j] = jug;
            }
        }
        status = false;

        // Comprobar Diagonal izq Arriba
        for (i = mov.fila - 1, j = mov.columna - 1; i >= 0 && j >= 0; i--, j--) {

            if (tab[i][j] == 0) {
                break;
            }
            if (tab[i][j] == jug) {
                status = true;
                break;
            }
        }
        if (status) {
            for (i++, j++; i != mov.fila; i++, j++) {
                tab[i][j] = jug;
            }
        }
        status = false;

        // Comprobar Diagonal der Arriba
        for (i = mov.fila - 1, j = mov.columna + 1; i >= 0 && j < 8; i--, j++) {

            if (tab[i][j] == 0) {
                break;
            }
            if (tab[i][j] == jug) {
                status = true;
                break;
            }
        }
        if (status) {
            for (i++, j--; i != mov.fila; i++, j--) {
                tab[i][j] = jug;
            }
        }
        status = false;

        // Comprobar Diagonal der abajo
        for (i = mov.fila + 1, j = mov.columna - 1; i < 8 && j >= 0; i++, j--) {

            if (tab[i][j] == 0) {
                break;
            }
            if (tab[i][j] == jug) {
                status = true;
                break;
            }
        }
        if (status) {
            for (i--, j++; i != mov.fila; i--, j++) {
                tab[i][j] = jug;
            }
        }
        status = false;

    }

    @Override
    public boolean esValido(Movimiento m) {
        // TODO Auto-generated method stub

        return movimientosValidos().contains(m);

    }
    /**
     * Devuelve las casillas ocupadas por el jugador que se pasa por argumento
     * @param sig - Jugador que no esta jugando
     * @return Array de Movimientos
     */
    private ArrayList<Movimiento> ocupadasJugador(int sig) {
        ArrayList<Movimiento> aux = new ArrayList<Movimiento>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.tab[i][j] == sig) {
                    aux.add(new MovimientoOthello(i, j));
                }
            }
        }

        return aux;
    }
    /**
     * Comprueba si el movimiento es valido horizontalmente
     * @param colAux Columna donde se quiere comprobar si se puede poner
     * @param colMov Columna donde esta la ficha
     * @param fila Fila
     * @param jug Jugador actual
     * @return true si puede mover, false lo contrario
     */
    private boolean compHorizontal(int colAux, int colMov, int fila, int jug) {

        boolean status = false;
        if (colAux < colMov) {
            for (int j = colAux + 1; j < colMov; j++) {
                if (this.tab[fila][j] == 0 || this.tab[fila][j] == jug) {
                    return false;
                }
                if (!status) {
                    status = true;
                }
            }
        } else {
            for (int j = colAux - 1; j > colMov; j--) {
                if (this.tab[fila][j] == 0 || this.tab[fila][j] == jug) {
                    return false;
                }
                if (!status) {
                    status = true;
                }
            }

        }
        return status;
    }
    /**
     * Comprueba si el movimiento es valido verticalmente
     * @param filAux Fila donde se quiere comprobar si se puede poner
     * @param filMov Fila donde esta la ficha
     * @param col Columna
     * @param jug Jugador actual
     * @return true si puede mover, false lo contrario
     */
    private boolean compVertical(int filAux, int filMov, int col, int jug) {
        boolean status = false;
        if (filAux < filMov) {
            for (int i = filAux + 1; i < filMov; i++) {
                if (this.tab[i][col] == 0 || this.tab[i][col] == jug) {
                    return false;
                }
                if (!status) {
                    status = true;
                }

            }
        } else {
            for (int i = filAux - 1; i > filMov; i--) {
                if (this.tab[i][col] == 0 || this.tab[i][col] == jug) {
                    return false;
                }
                if (!status) {
                    status = true;
                }
            }

        }
        return status;
    }
    /**
     *
     * @param filAux Fila donde se quiere comprobar si se puede poner
     * @param colAux Columna donde se quiere comprobar si se puede poner
     * @param mov Movimiento que representa donde esta la ficha
     * @param jug Jugador actual
     * @return true si puede mover, false lo contrario
     */
    private boolean compDiagonal(int filAux, int colAux, MovimientoOthello mov, int jug) {
        int i, j;
        boolean status = false;
        if (filAux > mov.fila && colAux > mov.columna) {

            for (i = filAux - 1, j = colAux - 1; i > mov.fila; i--, j--) {
                if (this.tab[i][j] == 0 || this.tab[i][j] == jug) {
                    return false;
                }
                if (!status) {
                    status = true;
                }
            }
        } else if (filAux < mov.fila && colAux < mov.columna) {

            for (i = filAux + 1, j = colAux + 1; i < mov.fila; i++, j++) {
                if (this.tab[i][j] == 0 || this.tab[i][j] == jug) {
                    return false;
                }
                if (!status) {
                    status = true;
                }
            }
        } else if (filAux > mov.fila && colAux < mov.columna) {

            for (i = filAux - 1, j = colAux + 1; i > mov.fila; i--, j++) {
                if (this.tab[i][j] == 0 || this.tab[i][j] == jug) {
                    return false;
                }
                if (!status) {
                    status = true;
                }
            }
        } else {
            for (i = filAux + 1, j = colAux - 1; i < mov.fila; i++, j--) {

                if (this.tab[i][j] == 0 || this.tab[i][j] == jug) {
                    return false;
                }
                if (!status) {
                    status = true;
                }
            }
        }

        return status;

    }
    /**
     *  Comprueba si el movimient
     * @param i Fila del movimiento posible
     * @param j Columna del movimiento posible
     * @param mov Lugar de la ficha en el tablero
     * @param jug Jugador actual
     * @return true si puede mover, false lo contrario
     */
    private boolean comprobar(int i, int j, MovimientoOthello mov, int jug) {

        return (i == mov.fila && compHorizontal(j, mov.columna, mov.fila, jug))
                || (j == mov.columna && compVertical(i, mov.fila, mov.columna, jug))
                || (Math.abs(i - mov.fila) == Math.abs(j - mov.columna) && compDiagonal(i, j, mov, jug));

    }

    @Override
    public ArrayList<Movimiento> movimientosValidos() {

        ArrayList<Movimiento> aux = new ArrayList<Movimiento>();
        int turnS = (getTurno() == 0) ? 1 : 2;
        ArrayList<Movimiento> casJ = ocupadasJugador(turnS);
        int i, j;

        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                if (this.tab[i][j] == 0) {
                    for (Movimiento x : casJ) {
                        MovimientoOthello mov = (MovimientoOthello) x;
                        if (comprobar(i, j, mov, turnS)) {
                            aux.add(new MovimientoOthello(i, j));
                            break;
                        }
                    }
                }
            }
        }
        if (aux.size() == 0) {
            aux.add(new MovimientoPasar());
        }
        return aux;
    }

    @Override
    public String tableroToString() {
        JSONObject object = new JSONObject();
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();
        String tablero= "";
        try {
           /* for(int i=0;i<size;i++){
                matrix.add(new ArrayList<Integer>(Arrays.asList(tab[i][0],tab[i][1],tab[i][2],tab[i][3],tab[i][4],tab[i][5],tab[i][6],tab[i][7])));
            }*/

            object.put(ID_ESTADO,this.estado);
            object.put(ID_NUM_JUG,this.numJugadas);
            object.put(ID_JUGADORES,this.numJugadores);
            for(int i=0;i<size;i++){
                for(int j=0;j<size;j++){
                   tablero+=String.valueOf(tab[i][j]);
                }
            }
            object.put(ID_TAB,tablero);
            object.put(ID_TURNO,this.turno);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


        return object.toString();
    }

    @Override
    public void stringToTablero(String cadena) throws ExcepcionJuego {
        ArrayList<ArrayList<Integer>> matrix;
        String cad;
        try {
            JSONObject object = new JSONObject(cadena);
            this.estado = object.getInt(ID_ESTADO);
            this.numJugadas = object.getInt(ID_NUM_JUG);
            this.numJugadores = object.getInt(ID_JUGADORES);
            cad= object.getString(ID_TAB);
            for(int i=0,c=0;i<size;i++){
                for(int j=0;j<size;j++){
                    this.tab[i][j]=Character.getNumericValue(cad.charAt(c));
                    c++;
                }
            }
            this.turno = object.getInt(ID_TURNO);
        } catch (JSONException e) {
            e.printStackTrace();
        }





    }

    @Override
    public String toString() {
        int ficha = (getTurno() == 0) ? 1 : 2;
        String s = "-------Turno de las fichas:" + ficha + "-------\n";
        int i, j;
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                s += this.tab[i][j] + " ";
            }
            s += "\n";
        }

        s += "--------------------\n";
        return s;

    }

    /**
     * Constructor del tablero
     * @param size tamano del tablero
     */
    public OthelloBoard(int size){
        super();
        this.tab = new int[8][8];

        this.estado = Tablero.EN_CURSO;

        this.tab[3][3] = 1;
        this.tab[3][4] = 2;
        this.tab[4][3] = 2;
        this.tab[4][4] = 1;

        this.size=size;
    }

    /**
     * Devuelve el valor que tenga el tablero en esa posicion
     * @param i fila
     * @param j columna
     * @return valor entre 0-2
     */
    public int getTablero(int i, int j) {
        return this.tab[i][j];
    }

    /**
     * Devuelve el numero fichas del Jugador 1
     * @return int
     */
    public int getNumFichasJ1(){

        int sum=0,i,j;

        for(i=0;i<size;i++){
            for(j=0;j<size;j++){
                if(this.tab[i][j]==JUGADOR1){
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

        int sum=0,i,j;

        for(i=0;i<size;i++){
            for(j=0;j<size;j++){
                if(this.tab[i][j]==JUGADOR2){
                    sum++;
                }
            }
        }

        return sum;
    }
}
