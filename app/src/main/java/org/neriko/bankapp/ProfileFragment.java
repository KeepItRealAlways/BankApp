package org.neriko.bankapp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ProfileFragment extends Fragment {

    private TextView balance;
    private TextView certificate;
    private TextView seminar;
    private TextView labs;
    private TextView lec_attended;
    private TextView lec_missed;
    private TextView next_fine;
    private TextView fac_info;

    private TextView LEC_MISSED;
    private TextView NEXT_FINE;
    private TextView FAC_INFO;

    private SwipeRefreshLayout swipeContainer;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        balance = view.findViewById(R.id.balance);
        certificate = view.findViewById(R.id.certificates);
        seminar = view.findViewById(R.id.seminar);
        labs = view.findViewById(R.id.labs);
        lec_attended = view.findViewById(R.id.lec_attended);
        lec_missed = view.findViewById(R.id.lec_missed);
        next_fine = view.findViewById(R.id.next_fine);
        fac_info = view.findViewById(R.id.fac_info);

        NEXT_FINE = view.findViewById(R.id.textView10);
        LEC_MISSED = view.findViewById(R.id.textView7);
        FAC_INFO = view.findViewById(R.id.textView3);

        updateViews();

        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ProfileActivity.getFragmentsInterface().requestProfileUpdate();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

    public void updateViews() {
        balance.setText(AppShared.getBalance());
        certificate.setText(AppShared.getCertificate());
        seminar.setText(AppShared.getSeminarInfo());
        labs.setText(AppShared.getLabInfo());
        lec_attended.setText(AppShared.getLecturesAttended());
        lec_missed.setText(AppShared.getLecturesMissed());
        next_fine.setText(AppShared.getNextFine());
        fac_info.setText(AppShared.getFacInfo());

        if (Integer.valueOf(AppShared.getLecturesMissed()) == 0) {
            lec_missed.setVisibility(View.GONE);
            next_fine.setVisibility(View.GONE);
            NEXT_FINE.setVisibility(View.GONE);
            LEC_MISSED.setVisibility(View.GONE);
        } else {
            lec_missed.setVisibility(View.VISIBLE);
            next_fine.setVisibility(View.VISIBLE);
            NEXT_FINE.setVisibility(View.VISIBLE);
            LEC_MISSED.setVisibility(View.VISIBLE);
        }

        if (AppShared.getFacRequired() != 0) {
            FAC_INFO.setVisibility(View.VISIBLE);
            fac_info.setVisibility(View.VISIBLE);
        } else {
            fac_info.setVisibility(View.GONE);
            FAC_INFO.setVisibility(View.GONE);
        }

        if (swipeContainer != null) {
            swipeContainer.setRefreshing(false);
        }
    }
}
