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
 * Created by oscar on 14/04/17.
 */

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {


    private RoundRepository repository;
    private EditText usernameEditText;
    private EditText passEditText;
    private EditText passCheckEditText;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        usernameEditText = (EditText) findViewById(R.id.reg_username);
        passEditText = (EditText) findViewById(R.id.reg_pass);
        passCheckEditText = (EditText) findViewById(R.id.reg_passc);
        Button registerButton = (Button)findViewById(R.id.reg_button);
        registerButton.setOnClickListener(this);
        CheckBox box = (CheckBox) findViewById(R.id.reg_mostrar);
        box.setOnClickListener(this);

        repository = RoundRepositoryFactory.createRepository(RegistroActivity.this);
        if (repository == null)
            Toast.makeText(RegistroActivity.this, R.string.repository_opening_error,
                    Toast.LENGTH_SHORT).show();
    }

    public void onClick(View v) {
        final String playername = usernameEditText.getText().toString();
        final String password = passEditText.getText().toString();
        final String passwordC = passCheckEditText.getText().toString();
        final View vaux=v;
        RoundRepository.LoginRegisterCallback loginRegisterCallback =
                new RoundRepository.LoginRegisterCallback() {
                    @Override
                    public void onLogin(String playerId) {
                        OthelloPreferenceActivity.setPlayerUUID(RegistroActivity.this, playerId);
                        OthelloPreferenceActivity.setPlayerName(RegistroActivity.this, playername);
                        OthelloPreferenceActivity.setPlayerPassword(RegistroActivity.this, password);
                        startActivity(new Intent(RegistroActivity.this, RoundListActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(String error) {
                        Snackbar.make(vaux, error, Snackbar.LENGTH_SHORT).show();
                    }
                };


        switch (v.getId()) {

            case R.id.reg_button:
                if(password.equals(passwordC)){
                    repository.register(playername, password, loginRegisterCallback);
                }else{
                    passCheckEditText.setError("No coinciden las contraseñas");
                }

                break;
            case R.id.reg_mostrar:
                CheckBox checkbox1=(CheckBox)findViewById(R.id.reg_mostrar);
                if (checkbox1.isChecked()==true) {
                    passEditText.setTransformationMethod(null);
                    passCheckEditText.setTransformationMethod(null);
                }else{
                    passEditText.setTransformationMethod(new PasswordTransformationMethod());
                    passCheckEditText.setTransformationMethod(new PasswordTransformationMethod());

                }
                break;

        }
    }

}


