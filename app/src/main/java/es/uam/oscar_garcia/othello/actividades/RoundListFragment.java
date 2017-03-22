package es.uam.oscar_garcia.othello.actividades;


import android.content.Context;

import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;
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


import es.uam.oscar_garcia.othello.R;

import es.uam.oscar_garcia.othello.model.Round;
import es.uam.oscar_garcia.othello.model.RoundRepository;


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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_round:
                Round round = new
                        Round(Integer.parseInt(OthelloPreferenceActivity.getBoardSize(getActivity())));
                RoundRepository.get(getActivity()).addRound(round);
                updateUI();
                return true;
            case R.id.menu_item_settings:
                callbacks.onPreferencesSelected();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class RoundHolder extends RecyclerView.ViewHolder  {
        private TextView idTextView;
        private TextView j1TextView;
        private TextView j2TextView;
        private TextView dateTextView;
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
            j2TextView.setText("R "+Integer.toString(round.getBoard().getNumFichasJ2()));
            dateTextView.setText(String.valueOf(round.getDate()).substring(0,19));
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
                    public void onItemClick(View view, int position) {
                        Round round =
                                RoundRepository.get(getContext()).getRounds().get(position);
                        callbacks.onRoundSelected(round);
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
        RoundRepository repository = RoundRepository.get(getActivity());
        List<Round> rounds = repository.getRounds();

        if (roundAdapter == null) {
            roundAdapter = new RoundAdapter(rounds);
            roundRecyclerView.setAdapter(roundAdapter);
        } else {
            roundAdapter.notifyDataSetChanged();
        }
    }


}
