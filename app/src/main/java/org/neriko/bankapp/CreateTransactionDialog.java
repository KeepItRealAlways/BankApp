package org.neriko.bankapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.fragment.app.DialogFragment;

public class CreateTransactionDialog extends DialogFragment {

    private EditText name;
    private EditText description;
    private EditText value;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_transaction, null);

        name = view.findViewById(R.id.target);
        description = view.findViewById(R.id.description);
        value = view.findViewById(R.id.amount);

        builder.setView(view)
                .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new SendTransactionRequest(name.getText().toString(),
                                description.getText().toString(),
                                value.getText().toString()).execute();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { dismiss(); }
                });
        return builder.create();
    }

    public class SendTransactionRequest extends AsyncTask<Void, Void, Void> {

        private String body;

        public SendTransactionRequest(String name, String description, String value) {
            JSONObject object = new JSONObject();
            try {
                object.put("creator", AppShared.getLogin());
                object.put("description", description);
                object.put("transaction_type", "p2p");
                JSONArray money = new JSONArray();
                JSONObject val = new JSONObject();
                val.put("receiver", name);
                val.put("value", Integer.valueOf(value));
                val.put("money_type", "p2p");
                money.put(val);
                object.put("money", money);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            body = object.toString();
        }

        @Override
        protected Void doInBackground(Void... params) {
            OutputStream out = null;

            try {
                URL url = new URL(AppShared.getUrl() + "/add_transaction/");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Cookie", "sessionid=" + AppShared.getAuthToken());
                out = new BufferedOutputStream(urlConnection.getOutputStream());

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(body);
                writer.flush();
                writer.close();
                out.close();

                urlConnection.connect();

                System.out.println(urlConnection.getResponseMessage());

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return null;
        }
    }
}
