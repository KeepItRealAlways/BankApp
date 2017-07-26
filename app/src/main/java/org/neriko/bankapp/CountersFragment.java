package org.neriko.bankapp;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CountersFragment extends Fragment {

    private TextView infoSem;
    private TextView infoLab;
    private TextView infoLecAtt;
    private TextView infoLecMis;
    private TextView infoLecPen;
    private TextView infoFac;

    private RecyclerView countersHolder;
    private List<Counter> counters;
    private CountersAdapter adapter;

    private SwipeRefreshLayout swipeContainer;

    public CountersFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_counters, container, false);

        infoSem = (TextView) view.findViewById(R.id.info_sem);
        infoLab = (TextView) view.findViewById(R.id.info_lab);
        infoLecAtt = (TextView) view.findViewById(R.id.info_lec_att);
        infoLecMis = (TextView) view.findViewById(R.id.info_lec_mis);
        infoLecPen = (TextView) view.findViewById(R.id.info_lec_pen);
        infoFac = (TextView) view.findViewById(R.id.info_fac);

        countersHolder = (RecyclerView) view.findViewById(R.id.counters);

        counters = new ArrayList<>();
        adapter = new CountersAdapter(getActivity(), counters);
        countersHolder.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        countersHolder.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), manager.getOrientation());
        countersHolder.addItemDecoration(dividerItemDecoration);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
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

    public void updateViews(final String info_sem,
                            final String info_lab,
                            final String info_lec_att,
                            final String info_lec_mis,
                            final String info_lec_pen,
                            final String info_fac,
                            final List<Counter> counters) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                infoSem.setText(info_sem);
                infoLab.setText(info_lab);
                infoLecAtt.setText(info_lec_att);

                infoLecPen.setVisibility(View.GONE);
                infoFac.setVisibility(View.GONE);
                infoLecMis.setVisibility(View.GONE);

                if (info_lec_pen != null) {
                    infoLecPen.setVisibility(View.VISIBLE);
                    infoLecPen.setText(info_lec_pen);
                }

                if (info_lec_mis != null) {
                    infoLecMis.setVisibility(View.VISIBLE);
                    infoLecMis.setText(info_lec_mis);
                }

                if (info_fac != null) {
                    infoFac.setVisibility(View.VISIBLE);
                    infoFac.setText(info_fac);
                }

                CountersFragment.this.counters.clear();
                for (Counter counter:counters) {
                    CountersFragment.this.counters.add(counter);
                }
                adapter.notifyDataSetChanged();

                swipeContainer.setRefreshing(false);
            }
        });
    }

    public class CountersAdapter extends
            RecyclerView.Adapter<CountersAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public TextView description;
            public TextView date;
            public TextView value;

            public ViewHolder(View itemView) {

                super(itemView);

                title = (TextView) itemView.findViewById(R.id.counter_title);
                description = (TextView) itemView.findViewById(R.id.counter_description);
                date = (TextView) itemView.findViewById(R.id.counter_date);
                value = (TextView) itemView.findViewById(R.id.counter_value);
            }
        }

        private List<Counter> counters;
        private Context context;

        public CountersAdapter(Context context, List<Counter> counters) {
            this.counters = counters;
            this.context = context;
        }

        private Context getContext() {
            return context;
        }

        @Override
        public CountersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View contactView = inflater.inflate(R.layout.counter_item, parent, false);

            CountersAdapter.ViewHolder viewHolder = new CountersAdapter.ViewHolder(contactView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CountersAdapter.ViewHolder viewHolder, int position) {
            Counter counter = counters.get(position);

            TextView title = viewHolder.title;
            title.setText(counter.title);
            TextView description = viewHolder.description;
            description.setText(counter.description);
            TextView date = viewHolder.date;
            date.setText(counter.date);

            long valueLong = Math.round(Double.valueOf(counter.value));
            if (valueLong != 1) {
                viewHolder.value.setVisibility(View.VISIBLE);
                viewHolder.value.setText(valueLong + "&");
                if (valueLong >= 0) {
                    viewHolder.value.setTextColor(getResources().getColor(R.color.green));
                } else {
                    viewHolder.value.setTextColor(getResources().getColor(R.color.red));
                }
            } else {
                viewHolder.value.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return counters.size();
        }
    }
}
