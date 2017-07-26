package org.neriko.bankapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText loginField;
    private EditText passwordField;

    private AppCompatButton loginButton;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginField = (EditText) findViewById(R.id.input_login);
        passwordField = (EditText) findViewById(R.id.input_password);

        prefs = getSharedPreferences("credentials", MODE_PRIVATE);

        loginButton = (AppCompatButton) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new AppCompatButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                String login = loginField.getText().toString();
                String password = passwordField.getText().toString();

                try {
                    AppShared.setAuthToken(new AuthTask(login, password).execute().get(10, TimeUnit.SECONDS));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (AppShared.getAuthToken() != null) {
                    prefs.edit().putString("login", login).putString("password", password).apply();
                    startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                    finish();
                } else {
                    Snackbar.make(findViewById(R.id.main_layout), getString(R.string.login_failed), Snackbar.LENGTH_LONG);
                }
            }
        });
    }
}
