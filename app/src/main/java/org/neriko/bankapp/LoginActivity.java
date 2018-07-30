package org.neriko.bankapp;

import android.content.Intent;
import android.content.SharedPreferences;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {

    private EditText loginField;
    private EditText passwordField;

    private AppCompatButton loginButton;

    private SharedPreferences prefs;

    private String login;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginField = findViewById(R.id.input_login);
        passwordField = findViewById(R.id.input_password);

        prefs = getSharedPreferences("credentials", MODE_PRIVATE);

        loginButton = findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new AppCompatButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                login = loginField.getText().toString();
                password = passwordField.getText().toString();

                if (!login.isEmpty()) {
                    if (!password.isEmpty()) {
                        new AuthTask(LoginActivity.this, login, password).execute();
                        findViewById(R.id.loginProgressBar).setVisibility(View.VISIBLE);
                    } else {
                        Snackbar.make(findViewById(R.id.linear_layout), R.string.input_password, Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(findViewById(R.id.linear_layout), R.string.input_login, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    public void returnAuthToken(String token){
        AppShared.setAuthToken(token);
        findViewById(R.id.loginProgressBar).setVisibility(View.GONE);
        if (AppShared.getAuthToken() != null) {
            AppShared.setLogin(login);
            prefs.edit().putString("login", login).putString("password", password).apply();
            startActivity(new Intent(LoginActivity.this, SplashActivity.class));
            finish();
        } else {
            Snackbar.make(findViewById(R.id.linear_layout), getString(R.string.login_failed), Snackbar.LENGTH_LONG).show();
        }
    }
}
