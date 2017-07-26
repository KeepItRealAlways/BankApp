package org.neriko.bankapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        if (sharedPreferences.contains("login") && sharedPreferences.contains("password")) {
            String login = sharedPreferences.getString("login", null);
            String password = sharedPreferences.getString("password", null);

            try {
                AppShared.setAuthToken(new AuthTask(login, password).execute().get(10, TimeUnit.SECONDS));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (AppShared.getAuthToken() != null) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
            } else {
                Snackbar.make(findViewById(R.id.main_layout), getString(R.string.login_failed), Snackbar.LENGTH_LONG).show();
            }
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}
