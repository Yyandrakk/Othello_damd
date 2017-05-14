package es.uam.oscar_garcia.othello.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import es.uam.eps.multij.AccionMover;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Tablero;
import es.uam.oscar_garcia.othello.actividades.OthelloPreferenceActivity;
import es.uam.oscar_garcia.othello.server.ServerInterface;
import es.uam.oscar_garcia.othello.views.OthelloView;

/**
 * Created by oscar on 10/05/17.
 */

public class OthelloRemotePlayer implements Jugador, OthelloView.OnPlayListener {

    String roundId;
    Context context;
    public OthelloRemotePlayer(Context context, String roundId) {
        this.context = context;
        this.roundId = roundId;
    }
    @Override
    public String getNombre() {
        return null;
    }

    @Override
    public boolean puedeJugar(Tablero tablero) {
        return false;
    }

    @Override
    public void onCambioEnPartida(Evento evento) {
        final ServerInterface is = ServerInterface.getServer(context);
        Response.Listener<JSONObject> responseListener = new
                Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String codeboard;
                        String turno;
                        try {
                           codeboard = response.getString("codedboard");
                          turno  = response.getString("turn");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }


                        Response.Listener<String> callback = new Response.Listener<String>(){
                            public void onResponse(String response) {


                            }

                        };
                        Response.ErrorListener error = new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        };

                        is.sendBoard(Integer.parseInt(roundId),OthelloPreferenceActivity.getPlayerUUID(context),codeboard,callback,error);
                    }

                };
        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
        if(evento.getTipo()==Evento.EVENTO_TURNO){
            is.esMiTurno(Integer.parseInt(roundId), OthelloPreferenceActivity.getPlayerUUID(context),responseListener,errorListener);
        }

    }

    @Override
    public void onPlay(int i, int j) {

    }
}
