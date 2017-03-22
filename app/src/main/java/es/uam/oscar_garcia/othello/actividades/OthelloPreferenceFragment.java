package es.uam.oscar_garcia.othello.actividades;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import es.uam.oscar_garcia.othello.R;

/**
 * Created by oscar on 17/03/17.
 */

public class OthelloPreferenceFragment extends PreferenceFragment {
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

}
