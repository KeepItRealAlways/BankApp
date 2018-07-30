package org.neriko.bankapp;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class DownloadProfileInfo extends AsyncTask<Void, Void, Void> {

    private Activity caller;

    public DownloadProfileInfo(Activity caller) {
        this.caller = caller;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        URL url;
        try {
            url = new URL(AppShared.getUrl() + "user/");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Cookie", "sessionid=" + AppShared.getAuthToken());
            int responseCode = connection.getResponseCode();
            System.out.println(responseCode == HttpsURLConnection.HTTP_OK);

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                List<Counter> counters = new ArrayList<>();
                List<Transaction> transactions = new ArrayList<>();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = reader.readLine();
                JSONObject object = new JSONObject(line);

                String name = object.getString("first_name") + " " + object.getString("last_name");
                AppShared.setName(name);

                String balance;
                Double absoluteBalance = object.getDouble("balance");
                if (Math.abs(absoluteBalance) >= 10) {
                    balance = String.valueOf(absoluteBalance.intValue());
                } else {
                    balance = String.format(Locale.ENGLISH, "%.1f", absoluteBalance);
                }
                AppShared.setBalance(balance + "@");

                JSONArray array = object.getJSONArray("counters");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject arrObj = array.getJSONObject(i);
                    if (arrObj.getBoolean("counted")) {
                        counters.add(new Counter(
                                arrObj.getString("type"),
                                arrObj.getString("description"),
                                arrObj.getString("creation_timestamp"),
                                arrObj.getString("value")
                        ));
                    }
                }
                Collections.reverse(counters);

                array = object.getJSONArray("balance_changes");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject arrObj = array.getJSONObject(i);
                    if (arrObj.getBoolean("counted")) {
                        Double fuckingValue = arrObj.getDouble("value");
                        String fuckingValueString;
                        if (Math.abs(absoluteBalance) >= 10) {
                            fuckingValueString = String.valueOf(fuckingValue.intValue());
                        } else {
                            fuckingValueString = String.format(Locale.ENGLISH, "%.1f", fuckingValue);
                        }

                        transactions.add(new Transaction(
                                arrObj.getString("type"),
                                arrObj.getString("description"),
                                fuckingValueString,
                                arrObj.getString("creation_timestamp"),
                                arrObj.getString("creator")
                        ));
                    }
                }
                Collections.reverse(transactions);

                AppShared.setCounters(counters);
                AppShared.setTransactions(transactions);

                JSONObject study_info = object.getJSONObject("counters_value");

                int certificate = study_info.getJSONObject("val").getInt("book_certificate");
                AppShared.setCertificate(certificate + "&");

                int seminar_attend = study_info.getJSONObject("val").getInt("seminar_attend");
                int fac_attend = study_info.getJSONObject("val").getInt("fac_attend");
                int study_needed = study_info.getJSONObject("info").getInt("study_needed");
                AppShared.setSeminarInfo(seminar_attend, fac_attend, study_needed);

                int lab_pass = study_info.getJSONObject("val").getInt("lab_pass");
                int lab_pass_needed = study_info.getJSONObject("info").getInt("lab_pass_needed");
                AppShared.setLabInfo(lab_pass + " из " + lab_pass_needed);

                int lecture_attend = study_info.getJSONObject("val").getInt("lecture_attend");
                AppShared.setLecturesAttended(Integer.toString(lecture_attend));

                int lecture_miss = study_info.getJSONObject("val").getInt("lecture_miss");
                AppShared.setLecturesMissed(Integer.toString(lecture_miss));

                int next_missed_lec_fine = study_info.getJSONObject("info").getInt("next_missed_lec_fine");
                AppShared.setNextFine(Integer.toString(next_missed_lec_fine) + "@");

                int fac_pass_needed = study_info.getJSONObject("info").getInt("fac_pass_needed");
                int fac_pass = study_info.getJSONObject("val").getInt("fac_pass");
                AppShared.setFacInfo(fac_pass + " из " + fac_pass_needed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (caller instanceof SplashActivity) {
            ((SplashActivity) caller).returnProfileInfo();
        }

        if (caller instanceof ProfileActivity) {
            ((ProfileActivity) caller).returnProfileInfo();
        }
    }
}
