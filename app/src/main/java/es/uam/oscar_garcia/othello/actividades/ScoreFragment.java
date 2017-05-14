package es.uam.oscar_garcia.othello.actividades;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import es.uam.oscar_garcia.othello.R;
import es.uam.oscar_garcia.othello.model.OthelloBoard;
import es.uam.oscar_garcia.othello.model.Round;
import es.uam.oscar_garcia.othello.model.RoundRepository;
import es.uam.oscar_garcia.othello.model.RoundRepositoryFactory;
import android.support.v4.app.Fragment;

/**
 * Created by oscar on 16/04/17.
 */

public class ScoreFragment extends Fragment {


    public  ScoreFragment(){
        super();
    }
    private RecyclerView roundRecyclerView;
    private ScoreAdapter roundAdapter;




    public class ScoreHolder extends RecyclerView.ViewHolder  {
        private TextView idTextView;
        private TextView j1TextView;
        private TextView j2TextView;
        private TextView sTextView;
        private TextView dateTextView;
        private Round round;

        /**
         * Constructor asigna los elemento de la vista a variables
         * @param itemView
         */
        public ScoreHolder(View itemView) {
            super(itemView);
            idTextView = (TextView) itemView.findViewById(R.id.list_item_ids);
            sTextView = (TextView) itemView.findViewById(R.id.list_item_s) ;
            dateTextView = (TextView) itemView.findViewById(R.id.list_item_dates);
        }

        /**
         * Reasigna el valor correspondiente
         * @param round
         */
        public void bindRound(Round round){
            this.round = round;
            int j1=round.getBoard().getNumFichasJ1();
            int j2=round.getBoard().getNumFichasJ2();
            int score=j2-j1;
            idTextView.setText(round.getTitle());

            sTextView.setText("Score: "+Integer.toString(score));

            if(score==0){
                sTextView.setTextColor(Color.BLACK);
            }else if(score>0){
                sTextView.setTextColor(Color.GREEN);
            }else{
                sTextView.setTextColor(Color.RED);
            }
            dateTextView.setText(String.valueOf(round.getDate()).substring(0,19));
        }
    }

    public class ScoreAdapter extends RecyclerView.Adapter<ScoreHolder> {
        private List<Round> rounds;


        public ScoreAdapter(List<Round> rounds) {
            this.rounds = rounds;
        }

        @Override
        public ScoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_score, parent, false);
            return new ScoreHolder(view);
        }

        public void setRounds(List<Round> r){
            this.rounds=r;
        }

        @Override
        public void onBindViewHolder(ScoreHolder holder, int position) {
            Round round = rounds.get(position);
            holder.bindRound(round);
        }
        @Override
        public int getItemCount() {
            return rounds.size();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_list, container, false);

        this.setHasOptionsMenu(true);

        view.setBackgroundColor(Color.parseColor("#D1C4E9"));

        roundRecyclerView = (RecyclerView) view.findViewById(R.id.score_recycler_view);
        RecyclerView.LayoutManager linearLayoutManager = new
                LinearLayoutManager(getActivity());
        roundRecyclerView.setLayoutManager(linearLayoutManager);
        roundRecyclerView.setItemAnimator(new DefaultItemAnimator());


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
        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());

        RoundRepository.RoundsCallback callback =new RoundRepository.RoundsCallback() {
            @Override
            public void onResponse(List<Round> rounds) {
                List<Round> aux = new ArrayList<>();
                for (Round r :rounds) {
                    if(r.getBoard().getEstado()!= OthelloBoard.EN_CURSO){
                        aux.add(r);
                    }

                }

                if (roundAdapter == null) {
                    roundAdapter = new ScoreAdapter(aux);
                    roundRecyclerView.setAdapter(roundAdapter);
                } else {
                    roundAdapter.setRounds(aux);
                    roundAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String error) {
                //Snackbar.make(, error, Snackbar.LENGTH_SHORT).show();
            }
        };

        repository.getRounds(OthelloPreferenceActivity.getPlayerUUID(getActivity()),null,null,callback);


    }


}
