package es.uam.oscar_garcia.othello.actividades;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

    /**
     * Interfaz para comunicar fragment
     */
    public interface Callbacks {
        void onRoundUpdated(Round round);
    }

    /**
     * Constructor de la clase RoundFragment
     */
    public RoundFragment() {
        super();
    }

    /**
     * Vincula la actividad al fragmento
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) context;
    }

    /**
     * Desvincula al fragmento a la actividad
     */
    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    /**
     * Indetenfica al fragmento
     * @param roundId
     * @return
     */
    public static RoundFragment newInstance(String roundId) {
        Bundle args = new Bundle();
        args.putString(ARG_ROUND_ID, roundId);
        RoundFragment roundFragment = new RoundFragment();
        roundFragment.setArguments(args);
        return roundFragment;
    }

    /**
     *
     * @param savedInstanceState
     */
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
        rootView.setBackgroundColor(Color.parseColor("#D1C4E9"));
        return rootView;
    }

    /**
     * Inicia la actividad cuando la crea Android
     */
    @Override
    public void onStart() {
        super.onStart();
        startRound();
    }

    /**
     * Inicia una nueva partida y se la asigna a la vista
     */
    void startRound() {
        ArrayList<Jugador> players = new ArrayList<Jugador>();
        JugadorAleatorio randomPlayer = new JugadorAleatorio("Jugador aleatorio");
        ERLocalPlayer localPlayer = new ERLocalPlayer();
        players.add(randomPlayer);
        players.add(localPlayer);
        game = new Partida(round.getBoard(), players);
        game.addObservador(this);
        localPlayer.setPartida(game);
        boardView = (ERView) getView().findViewById(R.id.board_erview);
        boardView.setBoard(size, round.getBoard());
        boardView.setOnPlayListener(localPlayer);
        registerListener();
        if (game.getTablero().getEstado() == Tablero.EN_CURSO)
            game.comenzar();

    }

    /**
     * Inicia la funcionalidad del boton reset
     */
    void registerListener(){

        FloatingActionButton resetButton = (FloatingActionButton)
                getView().findViewById(R.id.reset_round_fab);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (round.getBoard().getEstado() != Tablero.EN_CURSO) {
                    Snackbar.make(getView(), R.string.round_already_finished,
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }

                round.setBoard(new ERBoard(8));
                boardView.setBoard(8,round.getBoard());
                boardView.invalidate();
                callbacks.onRoundUpdated(round);
                startRound();

                callbacks.onRoundUpdated(round);
                Snackbar.make(getView(), R.string.round_restarted,
                        Snackbar.LENGTH_SHORT).show();
            }
            });
    }

    /**
     * Actualiza el tablero o muestra como ha termiando la partida
     * @param evento
     */
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
                if(round.getBoard().getEstado() == Tablero.TABLAS){
                    Snackbar.make(getView(), R.string.draw, Snackbar.LENGTH_SHORT).show();
                }
                else if(round.getBoard().getTurno()==1){
                    Snackbar.make(getView(), R.string.win, Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(getView(), R.string.game_over, Snackbar.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
