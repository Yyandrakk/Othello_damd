package es.uam.oscar_garcia.othello.actividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.uam.oscar_garcia.othello.R;
import es.uam.oscar_garcia.othello.model.RoundRepository;
import es.uam.oscar_garcia.othello.model.RoundRepositoryFactory;

/**
 * Created by oscar on 29/03/17.
 */

public class LoginActivity extends Activity implements View.OnClickListener {
    private RoundRepository repository;
    private EditText usernameEditText;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (!OthelloPreferenceActivity.getPlayerName(this).
                equals(OthelloPreferenceActivity.PLAYERNAME_DEFAULT)){
            startActivity(new Intent(LoginActivity.this, RoundListActivity.class));
            finish();
            return;
        }
        usernameEditText = (EditText) findViewById(R.id.login_username);
        passwordEditText = (EditText) findViewById(R.id.login_password);
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        repository = RoundRepositoryFactory.createRepository(LoginActivity.this);
        if (repository == null)
            Toast.makeText(LoginActivity.this, R.string.repository_opening_error,
                    Toast.LENGTH_SHORT).show();
    }

    public void onClick(View v) {
        final String playername = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        RoundRepository.LoginRegisterCallback loginRegisterCallback =
                new RoundRepository.LoginRegisterCallback() {
                    @Override
                    public void onLogin(String playerId) {
                        ERPreferenceActivity.setPlayerUUID(LoginActivity.this, playerId);
                        ERPreferenceActivity.setPlayerName(LoginActivity.this, playername);
                        ERPreferenceActivity.setPlayerPassword(LoginActivity.this, password);
                        startActivity(new Intent(LoginActivity.this, RoundListActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(String error) {
                    }
                };
    }

    switch (v.getId()) {
        case R.id.login_button:
            repository.login(playername, password, loginRegisterCallback);
            break;
        case R.id.cancel_button:
            finish();
            break;
        case R.id.new_user_button:
            repository.register(playername, password, loginRegisterCallback);
            break;
    }

}
