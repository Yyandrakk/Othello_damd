package es.uam.oscar_garcia.othello.actividades;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import es.uam.oscar_garcia.othello.R;
import es.uam.oscar_garcia.othello.model.Round;


/**
 * Created by oscar on 1/03/17.
 */

public class RoundListActivity extends AppCompatActivity implements RoundListFragment.Callbacks,RoundFragment.Callbacks {
    private static final int SIZE = 8;
    private RecyclerView roundRecyclerView;
    private RoundListFragment.RoundAdapter roundAdapter;
    private DrawerLayout mDrawerLayout;
    MediaPlayer mediaPlayer ;
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
        mediaPlayer = MediaPlayer.create(this, R.raw.to_new_world);
        if(OthelloPreferenceActivity.getMusic(this)){
            mediaPlayer.start();
        }else{
            mediaPlayer.pause();
        }


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);
                        Intent intent=null;
                        // TODO: handle navigation
                        switch (menuItem.getItemId()){
                            case R.id.menubar_item_exit:
                             intent = new Intent(RoundListActivity.this, LoginActivity.class);
                                OthelloPreferenceActivity.setPlayerUUID(RoundListActivity.this, OthelloPreferenceActivity.PLAYERUUID_DEFAULT);
                                OthelloPreferenceActivity.setPlayerName(RoundListActivity.this, OthelloPreferenceActivity.PLAYERNAME_DEFAULT);
                                OthelloPreferenceActivity.setPlayerPassword(RoundListActivity.this, OthelloPreferenceActivity.PLAYERPASS_DEFAULT);
                                break;
                            case R.id.menubar_item_setting:
                                intent = new Intent(RoundListActivity.this, OthelloPreferenceActivity.class);
                                break;
                            case R.id.menubar_item_help:
                                intent = new Intent(RoundListActivity.this, HelpActivity.class);
                                break;
                            case R.id.menubar_item_score:
                                intent = new Intent(RoundListActivity.this,ScoreActivity.class);
                                break;

                            default:

                        }

                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        if(intent!=null)startActivity(intent);
                        return true;
                    }
                });

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
            Intent intent = RoundActivity.newIntent(this, round.getId(),round.getFirstPlayerName(),round.getTitle(),round.getDate(),round.getBoard().tableroToString());
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

    @Override
    public void onNewRoundAdded() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        RoundListFragment roundListFragment = (RoundListFragment)
                fragmentManager.findFragmentById(R.id.fragment_container);
        roundListFragment.updateUI();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
         if (id == android.R.id.home) {
             if( mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                 mDrawerLayout.closeDrawer(GravityCompat.START);
             }else{
                 mDrawerLayout.openDrawer(GravityCompat.START);
             }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer!=null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }


    }

}
