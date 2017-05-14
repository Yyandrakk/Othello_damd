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
import java.util.UUID;

import es.uam.eps.multij.Evento;
import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.JugadorAleatorio;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.PartidaListener;
import es.uam.eps.multij.Tablero;
import es.uam.oscar_garcia.othello.R;
import es.uam.oscar_garcia.othello.model.OthelloBoard;
import es.uam.oscar_garcia.othello.model.OthelloLocalServerPlayer;
import es.uam.oscar_garcia.othello.model.OthelloRemotePlayer;
import es.uam.oscar_garcia.othello.model.Round;
import es.uam.oscar_garcia.othello.model.RoundRepository;
import es.uam.oscar_garcia.othello.model.RoundRepositoryFactory;
import es.uam.oscar_garcia.othello.views.OthelloView;

import static es.uam.oscar_garcia.othello.actividades.RoundActivity.BOARDSTRING;


/**
 * Created by oscar on 1/03/17.
 */

public class RoundFragment extends Fragment implements PartidaListener {

    OthelloView boardView;
    public static final String DEBUG = "DEBUG";
    public static final String ARG_ROUND_ID = "es.uam.eps.dadm.er18.round_id";
    public static final String ARG_FIRST_PLAYER_NAME =
            "es.uam.eps.dadm.er18.first_player_name";
    public static final String ARG_ROUND_TITLE = "es.uam.eps.dadm.er18.round_title";
    public static final String ARG_ROUND_DATE = "es.uam.eps.dadm.er18.round_date";
    public static final String ARG_ROUND_BOARD = "es.uam.eps.dadm.er18.round_board";
    private int size=8;
    private Round round;
    private Partida game;

    private Callbacks callbacks;
    private String roundId;
    private String firstPlayerName;
    private String roundTitle;
    private String roundDate;
    private String roundB;

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
    public static RoundFragment newInstance(String roundId, String firstPlayerName,
                                            String roundTitle, String roundDate, String roundBoard) {
        Bundle args = new Bundle();
        args.putString(ARG_ROUND_ID, roundId);
        args.putString(ARG_FIRST_PLAYER_NAME, firstPlayerName);
        args.putString(ARG_ROUND_TITLE, roundTitle);
        args.putString(ARG_ROUND_DATE, roundDate);
        args.putString(ARG_ROUND_BOARD,roundBoard);

        RoundFragment roundFragment = new RoundFragment();
        roundFragment.setArguments(args);
        return roundFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ROUND_ID)) {
            roundId = getArguments().getString(ARG_ROUND_ID);
        }
        if (getArguments().containsKey(ARG_FIRST_PLAYER_NAME)) {
            firstPlayerName = getArguments().getString(ARG_FIRST_PLAYER_NAME);
        }
        if (getArguments().containsKey(ARG_ROUND_TITLE)) {
            roundTitle = getArguments().getString(ARG_ROUND_TITLE);
        }
        if (getArguments().containsKey(ARG_ROUND_DATE)) {
            roundDate = getArguments().getString(ARG_ROUND_DATE);
        }
        if (getArguments().containsKey(ARG_ROUND_BOARD)) {
            roundB = getArguments().getString(ARG_ROUND_BOARD);
        }
        round=createRound();
    }
    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ROUND_ID)) {
            String roundId = getArguments().getString(ARG_ROUND_ID);
            round = RoundRepository.get(getActivity()).getRound(roundId);
            size = round.getSize();
        }
    }*/

    private void updateRound() {
        //Round round = createRound();
        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
        RoundRepository.BooleanCallback callback = new RoundRepository.BooleanCallback() {
            @Override
            public void onResponse(boolean response) {
                if (response == false)
                    Snackbar.make(getView(), R.string.error_updating_round,
                            Snackbar.LENGTH_LONG).show();
            }
        };
        repository.updateRound(round, callback);
    }


    private Round createRound() {
        Round round = new Round(size, UUID.fromString(OthelloPreferenceActivity.getPlayerUUID(getActivity())),OthelloPreferenceActivity.getPlayerName(getActivity()));
        round.setPlayerUUID(OthelloPreferenceActivity.getPlayerUUID(getActivity()));
        round.setId(roundId);
        round.setFirstPlayerName("random");
        round.setSecondPlayerName(firstPlayerName);
        round.setDate(roundDate);
        round.setTitle(roundTitle);

        try {
            round.getBoard().stringToTablero(roundB);
        } catch (ExcepcionJuego excepcionJuego) {
            excepcionJuego.printStackTrace();
        }
        return round;
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
        if(OthelloPreferenceActivity.getOnline(getContext())){
            OthelloLocalServerPlayer localServerPlayer = new OthelloLocalServerPlayer(getContext(),round.getId());
            OthelloRemotePlayer remotePlayer = new OthelloRemotePlayer(getContext(),round.getId());
            if(round.getFirstPlayerName().equals(OthelloPreferenceActivity.getPlayerName(getContext()))){
                players.add(localServerPlayer);
                players.add(remotePlayer);
            }else{
                players.add(remotePlayer);
                players.add(localServerPlayer);
            }
            game =new Partida(round.getBoard(),players);
            game.addObservador(this);
            boardView = (OthelloView) getView().findViewById(R.id.board_erview);
            boardView.setBoard(size, round.getBoard());
            boardView.setOnPlayListener(localServerPlayer);

        }else {
            JugadorAleatorio randomPlayer = new JugadorAleatorio("Jugador aleatorio");
            OthelloLocalPlayer localPlayer = new OthelloLocalPlayer();
            players.add(randomPlayer);
            players.add(localPlayer);
            game = new Partida(round.getBoard(), players);
            game.addObservador(this);
            localPlayer.setPartida(game);
            boardView = (OthelloView) getView().findViewById(R.id.board_erview);
            boardView.setBoard(size, round.getBoard());
            boardView.setOnPlayListener(localPlayer);
        }
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

                round.setBoard(new OthelloBoard(8));
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
                updateRound();
                break;
            case Evento.EVENTO_FIN:
                boardView.invalidate();
                callbacks.onRoundUpdated(round);
                updateRound();
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
