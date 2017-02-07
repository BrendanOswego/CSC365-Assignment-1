package com.example.brendan.mainpackage;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.example.brendan.mainpackage.onboarding.EndFragment;
import com.example.brendan.mainpackage.onboarding.MainFragment;
import com.example.brendan.mainpackage.onboarding.StartFragment;

//REMINDER handle overflow of unnecessary information, check if count or limit is smaller

public class MainActivity extends FragmentActivity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigateToStartDate();
    }


    public void navigateToStartDate() {
        BaseFragment f = new StartFragment();
        FragmentManager m = getSupportFragmentManager();
        m.beginTransaction().
                replace(R.id.fragment_container, f, "startFragment")
                .addToBackStack(null)
                .commit();
    }


    public void navigatToEndDate() {
        BaseFragment f = new EndFragment();
        FragmentManager m = getSupportFragmentManager();
        m.beginTransaction().
                replace(R.id.fragment_container, f, "endFragment")
                .addToBackStack(null)
                .commit();

    }

    public void navigateToMain() {
        BaseFragment f = new MainFragment();
        FragmentManager m = getSupportFragmentManager();

        m.beginTransaction().
                replace(R.id.fragment_container, f, "mainFragment")
                .addToBackStack(null)
                .commit();

    }

}
