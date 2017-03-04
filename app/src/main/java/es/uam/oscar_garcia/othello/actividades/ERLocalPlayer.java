package es.uam.oscar_garcia.othello.actividades;


import es.uam.eps.multij.AccionMover;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;
import es.uam.oscar_garcia.othello.model.ERMovement;
import es.uam.oscar_garcia.othello.views.ERView;


public class ERLocalPlayer implements  Jugador, ERView.OnPlayListener {


    private int SIZE = 8;
    Partida game;
    public ERLocalPlayer() {
    }

    @Override
    public void onPlay(int row, int column) {
        try {
            if (game.getTablero().getEstado() != Tablero.EN_CURSO) {
                return;
            }
            ERMovement m;
            m = new ERMovement(row, column);
            game.realizaAccion(new AccionMover(this, m));
        } catch (Exception e) {
        }
    }

    @Override
    public void onCambioEnPartida(Evento evento) {
    }

    public void setPartida(Partida game) {
        this.game = game;
    }
    @Override
    public String getNombre() {
        return "Local player";
    }
    @Override
    public boolean puedeJugar(Tablero tablero) {
        return true;
    }


}