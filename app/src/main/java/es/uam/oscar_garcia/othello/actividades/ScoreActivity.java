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
 * Created by oscar on 16/04/17.
 */

public class ScoreActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_score);
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
                                intent = new Intent(ScoreActivity.this, LoginActivity.class);
                                OthelloPreferenceActivity.setPlayerUUID(ScoreActivity.this, OthelloPreferenceActivity.PLAYERUUID_DEFAULT);
                                OthelloPreferenceActivity.setPlayerName(ScoreActivity.this, OthelloPreferenceActivity.PLAYERNAME_DEFAULT);
                                OthelloPreferenceActivity.setPlayerPassword(ScoreActivity.this, OthelloPreferenceActivity.PLAYERPASS_DEFAULT);
                                break;
                            case R.id.menubar_item_setting:
                                intent = new Intent(ScoreActivity.this, OthelloPreferenceActivity.class);
                                break;
                            case R.id.menubar_item_help:
                                intent = new Intent(ScoreActivity.this, HelpActivity.class);
                                break;
                            case R.id.menubar_item_home:
                                intent = new Intent(ScoreActivity.this, RoundListActivity.class);
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
        Fragment fragment = fm.findFragmentById(R.id.fragment_container_score);
        if (fragment == null) {
            fragment = new ScoreFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container_score, fragment)
                    .commit();
        }

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
