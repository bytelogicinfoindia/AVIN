package com.avin.avin.allactivity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.view.MenuItem;

import com.avin.avin.R;
import com.avin.avin.fragment.HelpCenterHome;
import com.avin.avin.fragment.HomeFragment;
import com.avin.avin.fragment.booking.MyBookingFragment;
import com.avin.avin.fragment.ProfileFragment;
import com.avin.avin.otherclass.BaseActivity;

public class MainActivity extends BaseActivity {

    BottomNavigationView bottom_navigation;
    android.app.FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         getSupportActionBar().setDisplayShowTitleEnabled(false);
       // getSupportActionBar().setTitle("");
         getSupportActionBar().hide();

        bottom_navigation = (BottomNavigationView) findViewById(R.id.navigation);
        HomeFragment homeFragment = new HomeFragment();
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, homeFragment);
        transaction.commit();

        bottom_navigation.setOnNavigationItemSelectedListener(

                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.tab_home:
                                HomeFragment homeFragment = new HomeFragment();
                                transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame, homeFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                                break;

                            case R.id.tab_mybooking:
                                MyBookingFragment bookingFragment = new MyBookingFragment();
                                transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame, bookingFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();

                                break;
                            case R.id.tab_helps:
                                HelpCenterHome itemFragment = new HelpCenterHome();
                                transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame, itemFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();

                                break;
                            case R.id.tab_profile:
                                ProfileFragment profileFragment = new ProfileFragment();
                                transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame, profileFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                                //Intent intent=new Intent(MainActivity.this,PagerActivity.class);
                                //  startActivity(intent);
                                break;

                        }

                        return true;
                    }
                });
    }
}

