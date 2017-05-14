package es.uam.oscar_garcia.othello.server;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.UUID;

import es.uam.eps.multij.ExcepcionJuego;
import es.uam.oscar_garcia.othello.actividades.OthelloPreferenceActivity;
import es.uam.oscar_garcia.othello.actividades.RoundListActivity;
import es.uam.oscar_garcia.othello.model.Round;
import es.uam.oscar_garcia.othello.model.RoundRepository;

/**
 * Created by oscar on 21/04/17.
 */

public class ServerRepository implements RoundRepository {
    private static final String DEBUG = "ServerRepository";
    private static ServerRepository repository;
    private final Context context;
    private ServerInterface is;

    public static ServerRepository getInstance(Context context) {
        Log.d(DEBUG,"LLEGO!!!");
        if (repository == null)
            repository = new ServerRepository(context.getApplicationContext());
        return repository;
    }

    private ServerRepository(Context context) {
        this.context = context.getApplicationContext();
        is = ServerInterface.getServer(context);
    }



    @Override
    public void open() throws Exception {}
    @Override
    public void close() {}
    public void loginOrRegister(final String playerName, String password,
                                boolean register,
                                final RoundRepository.LoginRegisterCallback callback) {
        is.login(playerName,aplySHA1( password), null, register,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        String uuid = result.trim();
                        if (uuid.equals("-1") || uuid.length() < 10) {
                            callback.onError("Error loggin in user " + playerName);
                        } else {
                            callback.onLogin(uuid);
                            Log.d(DEBUG, "Logged in: " + result.trim());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getLocalizedMessage());
                    }
                });


    }
    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
    private static String aplySHA1(String pss)
    {
        MessageDigest crypt = null;
        try {
            crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(pss.getBytes("UTF-8"));
            return byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
    @Override
    public void login(String playerName, String password,
                      final RoundRepository.LoginRegisterCallback callback) {
        loginOrRegister(playerName, password, false, callback);
    }
    @Override
    public void register(String playerName, String password,
                         RoundRepository.LoginRegisterCallback callback) {
        loginOrRegister(playerName, password, true, callback);
    }
    private List<Round> roundsFromJSONArray(JSONArray response) {
        List<Round> rounds = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject o = response.getJSONObject(i);
                String codedboard = o.getString("codedboard");
                String id = o.getString("roundid");
                String date = o.getString("dateevent");
                String players = o.getString("playernames");
                String turno = o.getString("turn");
                Round round = new Round(8, UUID.fromString(OthelloPreferenceActivity.getPlayerUUID(context)),OthelloPreferenceActivity.getPlayerName(context));
                if(codedboard!=null){
                    round.getBoard().stringToTablero(codedboard);
                }
                round.setId(id);
                round.setDate(date);
                String player[]=players.split(",");
                if(player.length==1){
                    round.setFirstPlayerName(player[0]);
                }else{
                    round.setFirstPlayerName(player[0]);
                    round.setSecondPlayerName(player[1]);
                }

              //  round.setTurno(turno);

// Se rellenan los miembros de round

                rounds.add(round);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ExcepcionJuego excepcionJuego) {
                excepcionJuego.printStackTrace();
            }
        }
        return rounds; }
    @Override
    public void getRounds(String playeruuid, String orderByField, String group,
                          final RoundsCallback callback) {
        List<Round> rounds = new ArrayList<>();
        Response.Listener<JSONArray> responseCallback = new
                Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Round> rounds = roundsFromJSONArray(response);
                        callback.onResponse(rounds);
                        Log.d(DEBUG, "Rounds downloaded from server");
                    }
                };
        Response.ErrorListener errorCallback = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError("Error downloading rounds from server");
                Log.d(DEBUG, "Error downloading rounds from server");
            }
        };

        is.getOpenRounds(playeruuid, responseCallback, errorCallback);

    }
    public void getActiveRounds(String puuid, final RoundsCallback callback) {

        Response.Listener<JSONArray> responseCallback = new
                Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Round> rounds = roundsFromJSONArray(response);
                        callback.onResponse(rounds);
                        Log.d(DEBUG, "Rounds downloaded from server getActiveRounds");
                    }
                };
        Response.ErrorListener errorCallback = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError("Error downloading rounds from server");
                Log.d(DEBUG, "Error downloading rounds from server getActiveRounds" );
            }
        };

        is.getActiveRounds(puuid, responseCallback, errorCallback);
    }
    public void getAllRounds(final String playeruuid, final RoundsCallback callback) {
        final List<Round> Allrounds = new ArrayList<>();

        Response.Listener<JSONArray> responseCallback = new
                Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Round> aux;
                        aux = roundsFromJSONArray(response);
                       for(Round r:aux){
                           r.setActive(true);
                           Allrounds.add(r);
                       }
                        Log.d(DEBUG, "Rounds downloaded from server getAllRounds 1");
                        RoundsCallback inteCallback = new RoundsCallback() {

                                  public void onResponse(List<Round> rounds) {
                                        Allrounds.addAll(rounds);
                                        callback.onResponse(Allrounds);
                                        Log.d(DEBUG, "Rounds downloaded from server getAllRounds 2");

                                    }

                                    public void onError(String error){
                                        callback.onError("Error downloading rounds from server");
                                        Log.d(DEBUG, "Error downloading rounds from server getAllRounds" );
                                    }
                                };

                        getRounds(playeruuid,null, null, inteCallback);

                    }
                };
        Response.ErrorListener errorCallback = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError("Error downloading rounds from server");
                Log.d(DEBUG, "Error downloading rounds from server");
            }
        };

        is.getActiveRounds(playeruuid, responseCallback, errorCallback);
    }
    @Override
    public void addRound(final Round round, final BooleanCallback callback) {
        Response.Listener<String> responseCallback = new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        callback.onResponse(!response.equals("-1"));
                        Log.d(DEBUG, response);
                    }
                };
        Response.ErrorListener errorCallback = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(DEBUG, "Error addRound" );
            }
        };

        is.newRound(round.getPlayerUUID().toString(),round.getBoard().tableroToString(),responseCallback,errorCallback);


    }
    @Override
    public void updateRound(Round round, final BooleanCallback callback) {
        Response.Listener<String> responseCallback = new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String respuesta) {
                        callback.onResponse(!respuesta.equals("-1"));
                        Log.d(DEBUG, "Rounds downloaded from server getActiveRounds");
                    }
                };
        Response.ErrorListener errorCallback = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(DEBUG, "Error downloading rounds from server getActiveRounds" );
            }
        };

        is.sendBoard(Integer.parseInt(round.getId()),OthelloPreferenceActivity.getPlayerUUID(context),round.getBoard().tableroToString(),responseCallback,errorCallback);
    }
    public void addPlayerToRound(final Round round, final BooleanCallback callback) {
        Response.Listener<String> responseCallback = new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String respuesta) {
                        callback.onResponse(!respuesta.equals("0"));
                        Log.d(DEBUG, "Rounds downloaded from server getActiveRounds");
                    }
                };
        Response.ErrorListener errorCallback = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(DEBUG, "Error downloading rounds from server getActiveRounds" );
            }
        };

        is.addPlayerToRound(Integer.parseInt(round.getId()),OthelloPreferenceActivity.getPlayerUUID(context),responseCallback,errorCallback);

    }


}
