package es.uam.oscar_garcia.othello.actividades;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import es.uam.eps.multij.Partida;
import es.uam.oscar_garcia.othello.R;
import es.uam.oscar_garcia.othello.model.OthelloBoard;
import es.uam.oscar_garcia.othello.model.Round;


public class RoundActivity extends AppCompatActivity implements RoundFragment.Callbacks {

    public static final String BOARDSTRING = "es.uam.eps.dadm.er8.grid";

    public static final String EXTRA_ROUND_ID = "es.uam.eps.dadm.er18.round_id";
    public static final String EXTRA_FIRST_PLAYER_NAME =
            "es.uam.eps.dadm.er18.first_player_name";
    public static final String EXTRA_ROUND_TITLE = "es.uam.eps.dadm.er18.round_title";
    public static final String EXTRA_ROUND_DATE = "es.uam.eps.dadm.er18.round_date";
    public static final String EXTRA_ROUND_BOARD = "es.uam.eps.dadm.er18.round_board";
    private Partida game;
    private OthelloBoard board;
    private int size;
    private Round round;

    /**
     * Crea la actividad e inicializa los fragmentos necesarios
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            String roundId = getIntent().getStringExtra(EXTRA_ROUND_ID);
            String roudnPlayer= getIntent().getStringExtra(EXTRA_FIRST_PLAYER_NAME);
            String roundTitle = getIntent().getStringExtra(EXTRA_ROUND_TITLE);
            String roundDate = getIntent().getStringExtra(EXTRA_ROUND_DATE);
            String roundBoard = getIntent().getStringExtra(EXTRA_ROUND_BOARD);
            RoundFragment roundFragment = RoundFragment.newInstance(roundId,roudnPlayer,roundTitle,roundDate,roundBoard);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, roundFragment)
                    .commit();
        }
    }

    /**
     * Metodo para comunicar fragmentos
     * @param packageContext
     * @param roundId
     * @return
     */
    public static Intent newIntent(Context packageContext, String roundId, String firstPlayerName,
                                   String roundTitle, String roundDate, String roundBoard){
        Intent intent = new Intent(packageContext, RoundActivity.class);
        intent.putExtra(EXTRA_ROUND_ID, roundId);
        intent.putExtra(EXTRA_FIRST_PLAYER_NAME, firstPlayerName);
        intent.putExtra(EXTRA_ROUND_TITLE, roundTitle);
        intent.putExtra(EXTRA_ROUND_DATE, roundDate);
        intent.putExtra(EXTRA_ROUND_BOARD,roundBoard);
        return intent;
    }

    /**
     * Metodo del calback Round Fragment que no es necesario
     * @param round
     */
    @Override
    public void onRoundUpdated(Round round) {
    }
}
