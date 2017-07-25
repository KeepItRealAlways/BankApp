package org.neriko.bankapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Neriko on 25.07.2017.
 */

public class ProfileFragmentsInterface {

    private TransactionsFragment transactionsFragment;
    private CountersFragment countersFragment;

    //private Activity activity;

    public ProfileFragmentsInterface(TransactionsFragment transactionsFragment, CountersFragment countersFragment) {
        this.transactionsFragment = transactionsFragment;
        this.countersFragment = countersFragment;
    }

    public TransactionsFragment getTransactionsFragment() {
        return transactionsFragment;
    }

    public CountersFragment getCountersFragment() {
        return countersFragment;
    }

    public void requestProfileUpdate() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(AppShared.getUrl() + "user/");
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Cookie", "sessionid=" + AppShared.getAuthToken());
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK){

                        final List<Counter> counters = new ArrayList<>();
                        final List<Transaction> transactions = new ArrayList<>();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line = reader.readLine();
                        JSONObject object = new JSONObject(line);

                        final String name = object.getString("last_name") + " " + object.getString("first_name");

                        final String balance;
                        Double absoluteBalance = object.getDouble("balance");

                        if (Math.abs(absoluteBalance) >= 10) {
                            balance = String.valueOf(absoluteBalance.intValue());
                        } else {
                            balance = String.format(Locale.ENGLISH, "%.1f", absoluteBalance);
                        }

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

                        JSONObject study_info = object.getJSONObject("counters_value");

                        final int certificate = study_info.getJSONObject("val").getInt("book_certificate");

                        final String seminar = "Семинары/Факультативы: " + (study_info.getJSONObject("val").getInt("seminar_attend")
                                + study_info.getJSONObject("val").getInt("fac_attend")) + "(" + study_info.getJSONObject("val").getInt("seminar_attend")
                                + "/" + study_info.getJSONObject("val").getInt("fac_attend")
                                + ") из " + study_info.getJSONObject("info").getInt("study_needed");
                        final String otchet = "Отчёты по лабораторным работам: " + study_info.getJSONObject("val").getInt("lab_pass") + " из " + study_info.getJSONObject("info").getInt("lab_pass_needed");
                        final String lecture_visited = "Лекций посещено: " + study_info.getJSONObject("val").getInt("lecture_attend");
                        final int lec_skipped = study_info.getJSONObject("val").getInt("lecture_miss");
                        String lecture_skipped_nonfinal;
                        if (lec_skipped == 0) {
                            lecture_skipped_nonfinal = null;
                        } else {
                            lecture_skipped_nonfinal = "Лекций пропущено: " + lec_skipped;
                        }
                        final String lecture_skipped = lecture_skipped_nonfinal;
                        String next_skip_penalty_nonfinal;
                        if (lec_skipped == 0) {
                            next_skip_penalty_nonfinal = null;
                        } else {
                            next_skip_penalty_nonfinal = "Штраф за следующую пропущенную лекцию: " + study_info.getJSONObject("info").getInt("next_missed_lec_fine") + "@";
                        }
                        final String next_skip_penalty = next_skip_penalty_nonfinal;
                        final int fac_pass_needed = study_info.getJSONObject("info").getInt("fac_pass_needed");
                        String faculty_pass_needed_nonfinal;
                        if (fac_pass_needed == 0) {
                            faculty_pass_needed_nonfinal = null;
                        } else {
                            faculty_pass_needed_nonfinal = "Зачёты по факультативам: " +  study_info.getJSONObject("val").getInt("fac_pass") + "/" + fac_pass_needed;
                        }
                        final String faculty_pass_needed = faculty_pass_needed_nonfinal;

                        final String greeting = "Привет, " + name;
                        final String balanceS = "Баланс: " + balance + "@";
                        String certificate_nonfinal;
                        if (certificate == 0) {
                            certificate_nonfinal = null;
                        } else {
                            certificate_nonfinal = "Сертификаты: " + certificate + "&";
                        }
                        final String cerificateS = certificate_nonfinal;

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

                        countersFragment.updateViews(seminar, otchet, lecture_visited, lecture_skipped, next_skip_penalty, faculty_pass_needed, counters);
                        transactionsFragment.updateViews(greeting, balanceS, cerificateS, transactions);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
