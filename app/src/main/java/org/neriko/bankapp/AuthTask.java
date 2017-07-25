package org.neriko.bankapp;

import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Nikita Kartomin 2k17
 */

public class AuthTask extends AsyncTask<Void, Void, String> {

    private URL url;
    private String login;
    private String password;

    public AuthTask(String login, String password) {
        try {
            url = new URL(AppShared.getUrl() + "auth/");
        } catch (MalformedURLException exception) {
            System.out.println("Couldn't happen, but here is the stack trace:");
            exception.printStackTrace();
        }

        this.login = login;
        this.password = password;
    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            OutputStream ostream = connection.getOutputStream();

            JSONObject object = new JSONObject();
            object.put("login", login);
            object.put("password", password);
            ostream.write(object.toString().getBytes());
            ostream.flush();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = reader.readLine();
                object = new JSONObject(line);
                if (object.getBoolean("auth_success")) {
                    return object.getString("session_id");
                }
            }

            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
