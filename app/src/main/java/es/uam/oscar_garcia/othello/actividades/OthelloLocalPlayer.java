package es.uam.oscar_garcia.othello.actividades;


import es.uam.eps.multij.AccionMover;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;
import es.uam.oscar_garcia.othello.model.OthelloMovement;
import es.uam.oscar_garcia.othello.views.OthelloView;


public class OthelloLocalPlayer implements  Jugador, OthelloView.OnPlayListener {


    private int SIZE = 8;
    Partida game;

    /**
     * Constructor del Jugador Humano
     */
    public OthelloLocalPlayer() {
    }

    /**
     * Realiza la accion de mover si la partida sigue en curso
     * @param row fila del movimiento
     * @param column columna del movimiento
     */
    @Override
    public void onPlay(int row, int column) {
        try {
            if (game.getTablero().getEstado() != Tablero.EN_CURSO) {
                return;
            }
            OthelloMovement m;
            m = new OthelloMovement(row, column);
            game.realizaAccion(new AccionMover(this, m));
        } catch (Exception e) {
        }
    }

    /**
     * @deprecated
     * @param evento
     */
    @Override
    public void onCambioEnPartida(Evento evento) {
    }

    /**
     *  Asocia una nueva Partida al jugador
     * @param game
     */
    public void setPartida(Partida game) {
        this.game = game;
    }

    /**
     * Devuelve el nombre
     * @return String
     */
    @Override
    public String getNombre() {
        return "Local player";
    }

    /**
     * Indica si puede jugar al juego
     * @param tablero
     * @return True
     */
    @Override
    public boolean puedeJugar(Tablero tablero) {
        return true;
    }


}