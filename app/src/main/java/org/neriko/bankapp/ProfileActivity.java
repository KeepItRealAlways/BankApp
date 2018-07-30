package org.neriko.bankapp;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private static ProfileFragmentsInterface fragmentsInterface;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        fragmentsInterface = new ProfileFragmentsInterface(this, new TransactionsFragment(), new CountersFragment(), new ProfileFragment());

        prefs = getSharedPreferences("credentials", MODE_PRIVATE);

        setTitle(AppShared.getName());
    }

    public static ProfileFragmentsInterface getFragmentsInterface() {
        return fragmentsInterface;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.sign_out) {

            AppShared.setAuthToken(null);

            prefs.edit().remove("login").remove("password").apply();

            startActivity(new Intent(this, LoginActivity.class));
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return fragmentsInterface.getProfileFragment();
                case 1:
                    return fragmentsInterface.getTransactionsFragment();
                case 2:
                    return fragmentsInterface.getCountersFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0: return getString(R.string.profile);
                case 1: return getString(R.string.transactions);
                case 2: return getString(R.string.counters);
            }

            return super.getPageTitle(position);
        }
    }

    public void returnProfileInfo() {
        fragmentsInterface.getCountersFragment().updateViews();
        fragmentsInterface.getTransactionsFragment().updateViews();
        fragmentsInterface.getProfileFragment().updateViews();
    }
}
