package org.neriko.bankapp;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsFragment extends Fragment {

    private RecyclerView transactionsHolder;
    private List<Transaction> transactions;
    private TransactionsAdapter adapter;

    private FloatingActionButton fab;

    private boolean firstRun = true;

    private SwipeRefreshLayout swipeContainer;

    public TransactionsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        transactionsHolder = view.findViewById(R.id.transactions);

        transactions = new ArrayList<>();
        adapter = new TransactionsAdapter(getActivity(), transactions);
        transactionsHolder.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        transactionsHolder.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        transactionsHolder.addItemDecoration(dividerItemDecoration);

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

        fab = view.findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateTransactionDialog dialog = new CreateTransactionDialog();
                dialog.show(getFragmentManager(), "fucking dialog");
            }
        });

        updateViews();

        return view;
    }

    public void updateViews() {

        transactions.clear();
        transactions.addAll(AppShared.getTransactions());
        adapter.notifyDataSetChanged();

        swipeContainer.setRefreshing(false);
    }

    public class TransactionsAdapter extends
            RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public TextView description;
            public TextView cost;
            public TextView date;
            public TextView sender;

            public ViewHolder(View itemView) {

                super(itemView);

                title = (TextView) itemView.findViewById(R.id.transaction_title);
                description = (TextView) itemView.findViewById(R.id.transaction_description);
                cost = (TextView) itemView.findViewById(R.id.transaction_cost);
                date = (TextView) itemView.findViewById(R.id.transaction_date);
                sender = (TextView) itemView.findViewById(R.id.transaction_sender);
            }
        }

        private List<Transaction> mTransactions;
        private Context mContext;

        public TransactionsAdapter(Context context, List<Transaction> transactions) {
            mTransactions = transactions;
            mContext = context;
        }

        private Context getContext() {
            return mContext;
        }

        @Override
        public TransactionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View contactView = inflater.inflate(R.layout.transaction_item, parent, false);

            ViewHolder viewHolder = new ViewHolder(contactView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(TransactionsAdapter.ViewHolder viewHolder, int position) {
            Transaction transaction = mTransactions.get(position);

            TextView title = viewHolder.title;
            title.setText(transaction.title);
            TextView description = viewHolder.description;
            description.setText(transaction.description);
            TextView date = viewHolder.date;
            date.setText(transaction.date);
            TextView sender = viewHolder.sender;
            sender.setText(transaction.sender);

            TextView cost = viewHolder.cost;
            int costValue = Integer.valueOf(transaction.cost);
            if (costValue >= 0) {
                cost.setTextColor(getResources().getColor(R.color.green));
            } else {
                cost.setTextColor(getResources().getColor(R.color.red));
            }
            cost.setText(transaction.cost + "@");
        }

        @Override
        public int getItemCount() {
            return mTransactions.size();
        }
    }
}
