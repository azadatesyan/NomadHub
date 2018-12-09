package com.nomadhub.nomadhub;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public TabFragment1 tabFragment1;
    public TabFragment2 tabFragment2;
    public TabFragment3 tabFragment3;

    FragmentManager fragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showFragment(tabFragment1);
                    return true;
                case R.id.navigation_dashboard:
                    showFragment(tabFragment2);
                    return true;
                case R.id.navigation_notifications:
                    showFragment(tabFragment3);
                    return true;
            }
            return false;
        }
    };

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabFragment1 = new TabFragment1();
        tabFragment2 = new TabFragment2();
        tabFragment3 = new TabFragment3();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.activity_main_frame_layout,tabFragment1,"map")
                .add(R.id.activity_main_frame_layout,tabFragment2,"events")
                .add(R.id.activity_main_frame_layout,tabFragment3,"saved")
                .hide(tabFragment2)
                .hide(tabFragment3)
                .show(tabFragment1)
                .setPrimaryNavigationFragment(tabFragment1)
                .commit();

        mDrawerLayout = findViewById(R.id.activity_main_drawer_layout);
        NavigationView navigationView = findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        Toolbar toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.burgericon);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFragment(Fragment fragment) {

        fragmentManager.beginTransaction().hide(fragmentManager.getPrimaryNavigationFragment())
                .show(fragment)
                .setPrimaryNavigationFragment(fragment)
                .commit();

    }

    public void setActionBarTitle(String title){

        getSupportActionBar().setTitle("Find Nomads in " + title);

    }

}