package es.uam.oscar_garcia.othello.actividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import es.uam.oscar_garcia.othello.R;
import es.uam.oscar_garcia.othello.model.RoundRepository;
import es.uam.oscar_garcia.othello.model.RoundRepositoryFactory;

/**
 * Created by oscar on 29/03/17.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private RoundRepository repository;
    private EditText usernameEditText;
    private EditText passwordEditText;


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
        passwordEditText = (EditText) findViewById(R.id.login_registro);
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        Button registerButton = (Button) findViewById(R.id.new_user_button);
        registerButton.setOnClickListener(this);
        CheckBox box = (CheckBox) findViewById(R.id.login_mostrar);
        box.setOnClickListener(this);

        repository = RoundRepositoryFactory.createRepository(LoginActivity.this);
        if (repository == null)
            Toast.makeText(LoginActivity.this, R.string.repository_opening_error,
                    Toast.LENGTH_SHORT).show();
    }

    public void onClick(View v) {
        final String playername = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        final View vaux=v;
        RoundRepository.LoginRegisterCallback loginRegisterCallback =
                new RoundRepository.LoginRegisterCallback() {
                    @Override
                    public void onLogin(String playerId) {
                        OthelloPreferenceActivity.setPlayerUUID(LoginActivity.this, playerId);
                        OthelloPreferenceActivity.setPlayerName(LoginActivity.this, playername);
                        OthelloPreferenceActivity.setPlayerPassword(LoginActivity.this, password);
                        startActivity(new Intent(LoginActivity.this, RoundListActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(String error) {
                        Snackbar.make(vaux, error, Snackbar.LENGTH_SHORT).show();
                    }
                };


        switch (v.getId()) {
            case R.id.login_button:
                repository.login(playername, password, loginRegisterCallback);
                break;
            case R.id.new_user_button:
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
                break;
            case R.id.login_mostrar:
                CheckBox checkbox1=(CheckBox)findViewById(R.id.login_mostrar);
                if (checkbox1.isChecked()==true) {
                    passwordEditText.setTransformationMethod(null);
                }else{
                    passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
                }
                break;

        }
    }

}
