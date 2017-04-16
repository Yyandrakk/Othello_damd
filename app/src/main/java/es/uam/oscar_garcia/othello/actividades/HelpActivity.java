package es.uam.oscar_garcia.othello.actividades;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ProgressBar;

import es.uam.oscar_garcia.othello.R;

/**
 * Created by oscar on 16/04/17.
 */

public class HelpActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;

    protected void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_fragment);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

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
                                intent = new Intent(HelpActivity.this, LoginActivity.class);
                                OthelloPreferenceActivity.setPlayerUUID(HelpActivity.this, OthelloPreferenceActivity.PLAYERUUID_DEFAULT);
                                OthelloPreferenceActivity.setPlayerName(HelpActivity.this, OthelloPreferenceActivity.PLAYERNAME_DEFAULT);
                                OthelloPreferenceActivity.setPlayerPassword(HelpActivity.this, OthelloPreferenceActivity.PLAYERPASS_DEFAULT);
                                break;
                            case R.id.menubar_item_setting:
                                intent = new Intent(HelpActivity.this, OthelloPreferenceActivity.class);
                                break;
                            case R.id.menubar_item_home:
                                intent = new Intent(HelpActivity.this, RoundListActivity.class);
                                break;

                            default:

                        }

                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        if(intent!=null)startActivity(intent);
                        return true;
                    }
                });

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
}

