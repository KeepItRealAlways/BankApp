package org.neriko.bankapp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
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
import android.widget.ImageButton;
import android.widget.ImageView;
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
        if (counters != null) {
            counters.clear();
            counters.addAll(AppShared.getCounters());
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (swipeContainer != null) {
            swipeContainer.setRefreshing(false);
        }
    }

    public class CountersAdapter extends
            RecyclerView.Adapter<CountersAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public TextView description;
            public TextView date;
            public TextView value;
            public ImageView thumb_up;
            public ImageView thumb_down;

            public ViewHolder(View itemView) {

                super(itemView);

                title = itemView.findViewById(R.id.counter_title);
                description = itemView.findViewById(R.id.counter_description);
                date = itemView.findViewById(R.id.counter_date);
                value = itemView.findViewById(R.id.counter_value);
                thumb_down = itemView.findViewById(R.id.thumb_down);
                thumb_up = itemView.findViewById(R.id.thumb_up);
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
        public void onBindViewHolder(final CountersAdapter.ViewHolder viewHolder, int position) {
            final Counter counter = counters.get(position);

            TextView title = viewHolder.title;
            title.setText(counter.title);
            TextView description = viewHolder.description;
            description.setText(counter.description);
            TextView date = viewHolder.date;
            date.setText(counter.date);

            long valueLong = Math.round(Double.valueOf(counter.value));
            if (valueLong != 1) {
                viewHolder.value.setVisibility(View.VISIBLE);
                viewHolder.thumb_down.setVisibility(View.INVISIBLE);
                viewHolder.thumb_up.setVisibility(View.INVISIBLE);
                viewHolder.value.setText(valueLong + "&");
//                if (valueLong >= 0) {
//                    viewHolder.value.setTextColor(getResources().getColor(R.color.green));
//                } else {
//                    viewHolder.value.setTextColor(getResources().getColor(R.color.red));
//                }
                if (valueLong >= 0) {
                    viewHolder.value.setBackgroundResource(R.drawable.green_circle);
                } else {
                    viewHolder.value.setBackgroundResource(R.drawable.red_circle);
                }
            } else {
                viewHolder.value.setVisibility(View.INVISIBLE);
                if (counter.title.equalsIgnoreCase("Пропуск Лекции")) {
                    viewHolder.thumb_down.setVisibility(View.VISIBLE);
                    viewHolder.thumb_up.setVisibility(View.INVISIBLE);
                } else if (counter.title.equalsIgnoreCase("Отчет по лабораторной работе") ||
                        counter.title.equalsIgnoreCase("Отчёт по лабораторной работе")) {
                    viewHolder.thumb_up.setVisibility(View.VISIBLE);
                    viewHolder.thumb_down.setVisibility(View.INVISIBLE);
                }
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(getActivity())
                            .setTitle(counter.title)
                            .setMessage(counter.description)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override public void onClick(DialogInterface dialogInterface, int i) { }
                            }).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return counters.size();
        }
    }
}
