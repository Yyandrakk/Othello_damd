package es.uam.oscar_garcia.othello.actividades;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import java.util.ArrayList;

import es.uam.eps.multij.Evento;
import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.JugadorAleatorio;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.PartidaListener;
import es.uam.eps.multij.Tablero;
import es.uam.oscar_garcia.othello.R;
import es.uam.oscar_garcia.othello.model.ERBoard;
import es.uam.oscar_garcia.othello.model.Round;


public class RoundActivity extends AppCompatActivity implements RoundFragment.Callbacks {

    public static final String BOARDSTRING = "es.uam.eps.dadm.er8.grid";
    public static final String EXTRA_ROUND_ID = "es.uam.eps.dadm.er10.round_id";
    private Partida game;
    private ERBoard board;
    private int size;
    private Round round;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            String roundId = getIntent().getStringExtra(EXTRA_ROUND_ID);
            RoundFragment roundFragment = RoundFragment.newInstance(roundId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, roundFragment)
                    .commit();
        }
    }
    public static Intent newIntent(Context packageContext, String roundId){
        Intent intent = new Intent(packageContext, RoundActivity.class);
        intent.putExtra(EXTRA_ROUND_ID, roundId);
        return intent;
    }

    @Override
    public void onRoundUpdated(Round round) {
    }

/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
        String roundId = getIntent().getStringExtra(EXTRA_ROUND_ID);
        round = RoundRepository.get(this).getRound(roundId);
        size = round.getSize();
        TextView roundTitleTextView = (TextView) findViewById(R.id.round_title);
        roundTitleTextView.setText(round.getTitle());
    }


    @Override
    public void onClick(View v) {

    }

    void startRound() {
        ArrayList<Jugador> players = new ArrayList<Jugador>();
        JugadorAleatorio randomPlayer = new JugadorAleatorio("Random player");
        ERLocalPlayer ERLocalPlayer = new ERLocalPlayer();
        players.add(randomPlayer);
        players.add(ERLocalPlayer);
        board = new ERBoard(SIZE);
        game = new Partida(board, players);
        game.addObservador(this);
        ERLocalPlayer.setPartida(game);
        registerListeners(ERLocalPlayer);
        if (game.getTablero().getEstado() == Tablero.EN_CURSO)
            game.comenzar();
    }
/*
    private void updateUI() {
        ImageButton button;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                button = (ImageButton) findViewById(ids[i][j]);
                if (board.getTablero(i, j) == ERBoard.JUGADOR1)
                    button.setBackgroundResource(R.drawable.blue_button_48dp);
                else if (board.getTablero(i, j) == ERBoard.VACIO)
                    button.setBackgroundResource(R.drawable.void_button_48dp);
                else
                    button.setBackgroundResource(R.drawable.green_button_48dp);
            }
    }
    public void onCambioEnPartida(Evento evento) {
        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                updateUI();
                break;
            case Evento.EVENTO_FIN:
                updateUI();
                Snackbar.make(findViewById(R.id.round_title), R.string.game_over,
                        Snackbar.LENGTH_SHORT).show();
                break;
        }
    }*/
/*
    @Override
    public String getNombre() {
        return null;
    }

    @Override
    public boolean puedeJugar(Tablero tablero) {
        return false;
    }


    public void onSaveInstanceState(Bundle outState) {
        outState.putString(BOARDSTRING, board.tableroToString());
        super.onSaveInstanceState(outState);
    }
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            board.stringToTablero(savedInstanceState.getString(BOARDSTRING));
            updateUI();
        } catch (ExcepcionJuego excepcionJuego) {
            excepcionJuego.printStackTrace();
        }
    }
    public static Intent newIntent(Context packageContext, String roundId) {
        Intent intent = new Intent(packageContext, RoundActivity.class);
        intent.putExtra(EXTRA_ROUND_ID, roundId);
        return intent;
    }*/


}
