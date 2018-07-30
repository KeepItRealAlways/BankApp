package org.neriko.bankapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    private ProfileFragment profileFragment;
    private ProfileActivity activity;

    public ProfileFragmentsInterface(ProfileActivity activity, TransactionsFragment transactionsFragment, CountersFragment countersFragment, ProfileFragment profileFragment) {
        this.transactionsFragment = transactionsFragment;
        this.countersFragment = countersFragment;
        this.profileFragment = profileFragment;
        this.activity = activity;
    }

    public TransactionsFragment getTransactionsFragment() {
        return transactionsFragment;
    }
    public CountersFragment getCountersFragment() {
        return countersFragment;
    }
    public ProfileFragment getProfileFragment() {
        return profileFragment;
    }

    public void requestProfileUpdate() {
        new DownloadProfileInfo(activity).execute();
    }
}
