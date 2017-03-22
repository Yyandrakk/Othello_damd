package es.uam.oscar_garcia.othello.actividades;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import es.uam.oscar_garcia.othello.R;

/**
 * Created by oscar on 17/03/17.
 */

public class OthelloPreferenceActivity extends AppCompatActivity {

    public final static String BOARDSIZE_KEY = "boardsize";
    public final static String BOARDSIZE_DEFAULT = "0";
    public final static String MOVALI_KEY = "movalido";
    public final static boolean MOVALI_DEFAULT = true;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        OthelloPreferenceFragment fragment = new OthelloPreferenceFragment();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();

    }
    public static String getBoardSize(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(BOARDSIZE_KEY, BOARDSIZE_DEFAULT);
    }
    public static void setBoardsize(Context context, int size) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(OthelloPreferenceActivity.BOARDSIZE_KEY, size);
        editor.commit();
    }

    public static boolean getMovValido(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(MOVALI_KEY,MOVALI_DEFAULT);
    }


}
