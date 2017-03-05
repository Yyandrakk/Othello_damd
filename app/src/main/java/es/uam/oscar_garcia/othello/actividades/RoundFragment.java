package es.uam.oscar_garcia.othello.actividades;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.JugadorAleatorio;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.PartidaListener;
import es.uam.eps.multij.Tablero;
import es.uam.oscar_garcia.othello.R;
import es.uam.oscar_garcia.othello.model.ERBoard;
import es.uam.oscar_garcia.othello.model.Round;
import es.uam.oscar_garcia.othello.model.RoundRepository;
import es.uam.oscar_garcia.othello.views.ERView;


/**
 * Created by oscar on 1/03/17.
 */

public class RoundFragment extends Fragment implements PartidaListener {

    ERView boardView;
    public static final String ARG_ROUND_ID = "es.uam.eps.dadm.er10.round_id";
    private int size=8;
    private Round round;
    private Partida game;

    private Callbacks callbacks;
    public interface Callbacks {
        void onRoundUpdated(Round round);
    }

    public RoundFragment() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    public static RoundFragment newInstance(String roundId) {
        Bundle args = new Bundle();
        args.putString(ARG_ROUND_ID, roundId);
        RoundFragment roundFragment = new RoundFragment();
        roundFragment.setArguments(args);
        return roundFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ROUND_ID)) {
            String roundId = getArguments().getString(ARG_ROUND_ID);
            round = RoundRepository.get(getActivity()).getRound(roundId);
            size = round.getSize();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_round, container,
                false);
        TextView roundTitleTextView = (TextView)
                rootView.findViewById(R.id.round_title);
        roundTitleTextView.setText(round.getTitle());

        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        startRound();
    }


    void startRound() {
        ArrayList<Jugador> players = new ArrayList<Jugador>();
        JugadorAleatorio randomPlayer = new JugadorAleatorio("Random player");
        ERLocalPlayer localPlayer = new ERLocalPlayer();
        players.add(randomPlayer);
        players.add(localPlayer);
        game = new Partida(round.getBoard(), players);
        game.addObservador(this);
        localPlayer.setPartida(game);
        boardView = (ERView) getView().findViewById(R.id.board_erview);
        boardView.setBoard(size, round.getBoard());
        boardView.setOnPlayListener(localPlayer);
        if (game.getTablero().getEstado() == Tablero.EN_CURSO)
            game.comenzar();

    }


/*
    void startRound() {
        ArrayList<Jugador> players = new ArrayList<Jugador>();
        JugadorAleatorio randomPlayer = new JugadorAleatorio("Random player");
        ERLocalPlayer localPlayer = new ERLocalPlayer();
        players.add(randomPlayer);
        players.add(localPlayer);
        game = new Partida(round.getBoard(), players);
        game.addObservador(this);
        localPlayer.setPartida(game);
        boardView = (ERView) getView().findViewById(R.id.board_erview);
        boardView.setBoard(size, round.getBoard());
        boardView.setOnPlayListener(localPlayer);
        registerListeners();
        if (game.getTablero().getEstado() == Tablero.EN_CURSO)
            game.comenzar();
    }*/

    @Override
    public void onCambioEnPartida (Evento evento) {
        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                this.boardView.setBoard(8,round.getBoard());
                boardView.invalidate();
                callbacks.onRoundUpdated(round);
                break;
            case Evento.EVENTO_FIN:
                boardView.invalidate();
                callbacks.onRoundUpdated(round);
                Snackbar.make(getView(), R.string.game_over, Snackbar.LENGTH_SHORT).show();
                break;
        }
    }
}
