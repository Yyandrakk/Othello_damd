package es.uam.oscar_garcia.othello.actividades;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import es.uam.oscar_garcia.othello.R;

/**
 * Created by oscar on 17/03/17.
 */

public class OthelloPreferenceActivity extends AppCompatActivity {


    public final static String MOVALI_KEY = "movalido";
    public final static boolean MOVALI_DEFAULT = true;
    public final static String MUSIC_KEY = "music";
    public final static boolean MUSIC_DEFAULT = true;
    public static final String PLAYERNAME_KEY = "nameU";
    public static final String PLAYERNAME_DEFAULT = "Usuario";
    public static final String PLAYERPASS_KEY = "passU";
    public static final String PLAYERPASS_DEFAULT = "Usuario";
    public static final String PLAYERUUID_KEY = "uuidU";
    public static final String PLAYERUUID_DEFAULT = "910201";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        OthelloPreferenceFragment fragment = new OthelloPreferenceFragment();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();

    }

    public static boolean getMusic(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(MUSIC_KEY,MUSIC_DEFAULT);
    }
    public static boolean getMovValido(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(MOVALI_KEY,MOVALI_DEFAULT);
    }

    public static String getPlayerName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PLAYERNAME_KEY,PLAYERNAME_DEFAULT);
    }

    public static String getPlayerPass(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PLAYERPASS_KEY,PLAYERPASS_DEFAULT);
    }

    public static String getPlayerUUID(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PLAYERUUID_KEY,PLAYERUUID_DEFAULT);
    }


    public static void setPlayerUUID(Context context, String playerId) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OthelloPreferenceActivity.PLAYERUUID_KEY, playerId);
        editor.commit();
    }

    public static void setPlayerName(Context context, String playername) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OthelloPreferenceActivity.PLAYERNAME_KEY, playername);
        editor.commit();
    }

    public static void setPlayerPassword(Context context, String password) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OthelloPreferenceActivity.PLAYERPASS_KEY, password);
        editor.commit();
    }
}
