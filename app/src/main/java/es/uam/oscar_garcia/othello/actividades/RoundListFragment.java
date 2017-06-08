package es.uam.oscar_garcia.othello.actividades;


import android.content.Context;

import android.graphics.Color;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;


import es.uam.oscar_garcia.othello.R;

import es.uam.oscar_garcia.othello.database.OthelloDataBase;
import es.uam.oscar_garcia.othello.model.Round;
import es.uam.oscar_garcia.othello.model.RoundRepository;
import es.uam.oscar_garcia.othello.model.RoundRepositoryFactory;
import es.uam.oscar_garcia.othello.server.ServerRepository;


/**
 * Created by oscar on 1/03/17.
 */

public class RoundListFragment extends Fragment {


    public  RoundListFragment(){
        super();
    }
    private RecyclerView roundRecyclerView;
    private RoundAdapter roundAdapter;
    private Callbacks callbacks;




    public interface Callbacks {
        void onRoundSelected(Round round);
        void onPreferencesSelected();
        void onNewRoundAdded();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_round:
        /* Instanciar una partida e inicializarla adecuadamente
        Crear el repositorio
        Instanciar un método callback de tipo BooleanCallback y llamar a
        onNewRoundAdded de callbacks. */


              RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
                Round round = new Round(8, UUID.fromString(OthelloPreferenceActivity.getPlayerUUID(getActivity())),OthelloPreferenceActivity.getPlayerName(getActivity()));
                RoundRepository.BooleanCallback callback = new RoundRepository.BooleanCallback() {
                    @Override
                    public void onResponse(boolean response) {

                        if (response == false){
                            Snackbar.make(getView(), R.string.error_creating_round,
                                    Snackbar.LENGTH_LONG).show();
                        }else{
                            updateUI();
                            callbacks.onNewRoundAdded();
                        }

                    }
                };
                repository.addRound(round, callback);

                return true;
            /*case R.id.menu_item_settings:
                callbacks.onPreferencesSelected();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class RoundHolder extends RecyclerView.ViewHolder  {
        private TextView idTextView;
        private TextView j1TextView;
        private TextView j2TextView;
        private TextView dateTextView;
        private TextView nameTextView;
        private Round round;

        /**
         * Constructor asigna los elemento de la vista a variables
         * @param itemView
         */
        public RoundHolder(View itemView) {
            super(itemView);
            idTextView = (TextView) itemView.findViewById(R.id.list_item_id);
            j1TextView = (TextView) itemView.findViewById(R.id.list_item_j1);
            j2TextView = (TextView) itemView.findViewById(R.id.list_item_j2);
            nameTextView = (TextView) itemView.findViewById(R.id.list_item_name);
            dateTextView = (TextView) itemView.findViewById(R.id.list_item_date);
        }

        /**
         * Reasigna el valor correspondiente
         * @param round
         */
        public void bindRound(Round round){
            this.round = round;
            idTextView.setText(round.getTitle());
            j1TextView.setText("G "+Integer.toString(round.getBoard().getNumFichasJ1()));
            j1TextView.setTextColor(Color.parseColor("#7CB342"));
            j2TextView.setText("R "+Integer.toString(round.getBoard().getNumFichasJ2()));
            j2TextView.setTextColor(Color.parseColor("#FF5722"));
            dateTextView.setText(String.valueOf(round.getDate()).substring(0,19));
            nameTextView.setText(round.getFirstPlayerName());
            if(!round.getActive()){
                this.itemView.setBackgroundColor(Color.parseColor("#C5E1A5"));
            }
        }
    }

    public class RoundAdapter extends RecyclerView.Adapter<RoundHolder> {
        private List<Round> rounds;


        public RoundAdapter(List<Round> rounds) {
            this.rounds = rounds;
        }

        @Override
        public RoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_round, parent, false);
            return new RoundHolder(view);
        }

        public void setRounds(List<Round> r){
            this.rounds=r;
        }

        @Override
        public void onBindViewHolder(RoundHolder holder, int position) {
            Round round = rounds.get(position);
            holder.bindRound(round);
        }
        @Override
        public int getItemCount() {
            return rounds.size();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_round_list, container, false);

        this.setHasOptionsMenu(true);

        view.setBackgroundColor(Color.parseColor("#D1C4E9"));

        roundRecyclerView = (RecyclerView) view.findViewById(R.id.round_recycler_view);
        RecyclerView.LayoutManager linearLayoutManager = new
                LinearLayoutManager(getActivity());
        roundRecyclerView.setLayoutManager(linearLayoutManager);
        roundRecyclerView.setItemAnimator(new DefaultItemAnimator());


        roundRecyclerView.addOnItemTouchListener(new
                RecyclerItemClickListener(getActivity(), new
                RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, final int position) {
                        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
                        RoundRepository.RoundsCallback roundsCallback = new RoundRepository.RoundsCallback()
                        {
                            @Override
                            public void onResponse(List<Round> rounds) {
                                Round r = rounds.get(position);
                                if(r.getActive())
                                    callbacks.onRoundSelected(rounds.get(position));
                                else if(!(r.getFirstPlayerName().equals(OthelloPreferenceActivity.getPlayerName(getContext())))){
                                   RoundRepository s=RoundRepositoryFactory.createRepository(getContext());
                                    if(s instanceof ServerRepository){
                                        RoundRepository.BooleanCallback bcallback = new RoundRepository.BooleanCallback() {
                                            @Override
                                            public void onResponse(boolean ok) {

                                            }
                                        };
                                        ((ServerRepository) s).addPlayerToRound(r,bcallback);
                                    }

                                }
                            }
                            @Override
                            public void onError(String error) {
                                Snackbar.make(getView(), R.string.error_reading_rounds,
                                        Snackbar.LENGTH_LONG).show();
                            }
                        };
                        String playeruuid = OthelloPreferenceActivity.getPlayerUUID(getActivity());
                        if(repository instanceof OthelloDataBase)
                            repository.getRounds(playeruuid,null,null,roundsCallback);
                        else
                            ((ServerRepository) repository).getAllRounds(playeruuid,roundsCallback);
                        //repository.getRounds(playeruuid, null, null, roundsCallback);
                    }
                }));
    this.setHasOptionsMenu(true);

        updateUI();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    /**
     * Actualiza la interfaz grafica
     */
    protected void updateUI() {
        RoundRepository repository = RoundRepositoryFactory.createRepository(getContext());

        RoundRepository.RoundsCallback callback =new RoundRepository.RoundsCallback() {
            @Override
            public void onResponse(List<Round> rounds) {
                if (roundAdapter == null) {
                    roundAdapter = new RoundAdapter(rounds);
                    roundRecyclerView.setAdapter(roundAdapter);
                } else {
                    roundAdapter.setRounds(rounds);
                    roundAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String error) {
                //Snackbar.make(, error, Snackbar.LENGTH_SHORT).show();
            }
        };
        if(repository instanceof OthelloDataBase)
            repository.getRounds(OthelloPreferenceActivity.getPlayerUUID(getActivity()),null,null,callback);
        else
            ((ServerRepository) repository).getAllRounds(OthelloPreferenceActivity.getPlayerUUID(getActivity()),callback);

    }


}
