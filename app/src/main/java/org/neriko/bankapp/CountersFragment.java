package org.neriko.bankapp;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CountersFragment extends Fragment {

    private RecyclerView countersHolder;
    private List<Counter> counters = new ArrayList<>();
    private CountersAdapter adapter;

    private SwipeRefreshLayout swipeContainer;

    public CountersFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_counters, container, false);

        countersHolder = view.findViewById(R.id.counters);

        adapter = new CountersAdapter(getActivity(), counters);
        countersHolder.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        countersHolder.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), manager.getOrientation());
        countersHolder.addItemDecoration(dividerItemDecoration);

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

        updateViews();

        return view;
    }



    public void updateViews() {
        counters.clear();
        counters.addAll(AppShared.getCounters());
        adapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
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

                title = itemView.findViewById(R.id.counter_title);
                description = itemView.findViewById(R.id.counter_description);
                date = itemView.findViewById(R.id.counter_date);
                value = itemView.findViewById(R.id.counter_value);
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
