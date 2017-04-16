package es.uam.oscar_garcia.othello.actividades;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import es.uam.oscar_garcia.othello.R;

/**
 * Created by oscar on 16/04/17.
 */

public class LoadActivity extends AppCompatActivity {


        myTask mytask = new myTask();

        ProgressBar progressbar;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_load);

            progressbar = (ProgressBar) findViewById(R.id.progressBar);
            mytask.execute();
        }


        class myTask extends AsyncTask<String, String, String> {
            @Override
            protected void onPreExecute() {
                progressbar.setProgress(0);
                progressbar.setMax(100);
                int progressbarstatus = 0;
            }

            @Override
            protected String doInBackground(String... params) {
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                   this.publishProgress();
                }
                return "completed";
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                Intent intent = new Intent(LoadActivity.this,LoginActivity.class);
                startActivity(intent);

            }

            @Override
            protected void onProgressUpdate(String... values) {
                progressbar.incrementProgressBy(20);
                super.onProgressUpdate(values);
            }

        }

    }

