package es.uam.oscar_garcia.othello.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import es.uam.eps.multij.AccionMover;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;
import es.uam.oscar_garcia.othello.actividades.OthelloPreferenceActivity;
import es.uam.oscar_garcia.othello.server.ServerInterface;
import es.uam.oscar_garcia.othello.views.OthelloView;

/**
 * Created by oscar on 28/04/17.
 */

public class OthelloLocalServerPlayer implements Jugador, OthelloView.OnPlayListener {

    private static final String DEBUG = "DEBUG";
    private Partida game;
    private Context context;
    private String roundId;
    public OthelloLocalServerPlayer(Context context, String roundId) {
        this.context = context;
        this.roundId = roundId;
    }
    private boolean isBoardUpToDate(String codedboard) {
        return game.getTablero().tableroToString().equals(codedboard);
    }

    public void onPlay(final int row, final int column) {
       final ServerInterface is = ServerInterface.getServer(context);
        Response.Listener<JSONObject> responseListener = new
                Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
// Si el turno es del jugador y el tablero está actualizado realiza movimiento
// Si el turno es del jugador pero el tablero no está actualizado actualizar tablero
// Si el turno no es del jugador, mostrar mensaje

                            String codedboard = response.getString("codedboard");
                            String turno = response.getString("turn");
                            if(isBoardUpToDate(codedboard))
                            {
                                MovimientoOthello m;
                                m = new MovimientoOthello(row, column);
                                game.realizaAccion(new AccionMover(OthelloLocalServerPlayer.this, m));

                            }else{
                                game.getTablero().stringToTablero(codedboard);
                            }

                        } catch (Exception e) {
                            Log.d(DEBUG, "" + e);
                        }
                    }
                };
        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };

        is.esMiTurno(Integer.parseInt(roundId), OthelloPreferenceActivity.getPlayerUUID(context),responseListener,errorListener);


    }


    @Override
    public String getNombre() {
        return null;
    }

    @Override
    public boolean puedeJugar(Tablero tablero) {
        return true;
    }

    @Override
    public void onCambioEnPartida(Evento evento) {

    }
}
