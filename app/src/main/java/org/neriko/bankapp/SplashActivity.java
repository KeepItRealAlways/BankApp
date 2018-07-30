package org.neriko.bankapp;

import android.content.Intent;
import android.content.SharedPreferences;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (AppShared.getAuthToken() == null) {
            SharedPreferences sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
            if (sharedPreferences.contains("login") && sharedPreferences.contains("password")) {

                String login = sharedPreferences.getString("login", null);
                String password = sharedPreferences.getString("password", null);

                AppShared.setLogin(login);

                new AuthTask(this, login, password).execute();
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        } else {
            new DownloadProfileInfo(this).execute();
        }
    }

    public void returnAuthToken(String token){
        AppShared.setAuthToken(token);

        if (AppShared.getAuthToken() != null) {
            new DownloadProfileInfo(this).execute();
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }

    public void returnProfileInfo() {
        startActivity(new Intent(SplashActivity.this, ProfileActivity.class));
        finish();
    }
}
