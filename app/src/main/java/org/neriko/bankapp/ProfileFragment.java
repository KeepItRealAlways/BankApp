package org.neriko.bankapp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

    private TextView LEC_MISSED;
    private TextView NEXT_FINE;

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

        NEXT_FINE = view.findViewById(R.id.textView10);
        LEC_MISSED = view.findViewById(R.id.textView7);

        updateViews();

        return view;
    }

    public void updateViews() {
        balance.setText(AppShared.getBalance());
        certificate.setText(AppShared.getCertificate());
        seminar.setText(AppShared.getSeminarInfo());
        labs.setText(AppShared.getLabInfo());
        lec_attended.setText(AppShared.getLecturesAttended());

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
            lec_missed.setText(AppShared.getLecturesMissed());
            next_fine.setText(AppShared.getNextFine());
        }
    }

}
