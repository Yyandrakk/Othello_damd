package es.uam.oscar_garcia.othello.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import es.uam.oscar_garcia.othello.R;
import es.uam.oscar_garcia.othello.model.Round;


/**
 * Created by oscar on 1/03/17.
 */

public class RoundListActivity extends AppCompatActivity implements RoundListFragment.Callbacks,RoundFragment.Callbacks {
    private static final int SIZE = 8;
    private RecyclerView roundRecyclerView;
    private RoundListFragment.RoundAdapter roundAdapter;


    /**
     * Crear la actividad y los fragmentos asociados
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_fragment);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterdetail);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new RoundListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    /**
     * Inicializa la actividad o elfragmento para la ronda selecionada
     * @param round Round selecionada
     */
    @Override
    public void onRoundSelected(Round round) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = RoundActivity.newIntent(this, round.getId());
            startActivity(intent);
        } else {
            RoundFragment roundFragment = RoundFragment.newInstance(round.getId(),OthelloPreferenceActivity.getPlayerName(this),round.getTitle(),round.getDate(),round.getBoard().tableroToString());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, roundFragment)
                    .commit();
        }
    }

    /**
     * Actualiza los fragmentos de la lista
     * @param round Round modificada
     */
    public void onRoundUpdated(Round round) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        RoundListFragment roundListFragment = (RoundListFragment)
                fragmentManager.findFragmentById(R.id.fragment_container);
        roundListFragment.updateUI();
    }

    @Override
    public void onPreferencesSelected() {
        Intent intent = new Intent(this, OthelloPreferenceActivity.class);
        startActivity(intent);
    }

}
