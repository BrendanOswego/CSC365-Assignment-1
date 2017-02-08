package com.example.brendan.mainpackage;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.example.brendan.mainpackage.onboarding.EndFragment;
import com.example.brendan.mainpackage.onboarding.MainFragment;
import com.example.brendan.mainpackage.onboarding.StartFragment;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            if (getSupportFragmentManager().findFragmentByTag("startFragment") == null) {
                BaseFragment f = new StartFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, f, "startFragment")
                        .commit();
            }
        } else {
            System.out.println("Start Fragment is not null");
        }
    }


    public void navigateToStartDate() {
        BaseFragment f = new StartFragment();
        FragmentManager m = getSupportFragmentManager();
        if (m.findFragmentByTag("startFragment") == null) {
            m.beginTransaction()
                    .replace(R.id.fragment_container, f, "startFragment")
                    .commit();
        } else {
            m.beginTransaction()
                    .replace(R.id.fragment_container, m.findFragmentByTag("startFragment"))
                    .commit();
        }
    }

    public void navigatToEndDate() {
        BaseFragment f = new EndFragment();
        FragmentManager m = getSupportFragmentManager();
        if (m.findFragmentByTag("endFragment") == null) {
            m.beginTransaction()
                    .replace(R.id.fragment_container, f, "endFragment")
                    .commit();
        } else {
            m.beginTransaction()
                    .replace(R.id.fragment_container, m.findFragmentByTag("endFragment"))
                    .commit();
        }

    }

    public void navigateToMain() {
        BaseFragment f = new MainFragment();
        FragmentManager m = getSupportFragmentManager();

        if (m.findFragmentByTag("mainFragment") == null) {
            m.beginTransaction()
                    .replace(R.id.fragment_container, f, "mainFragment")
                    .commit();
        } else {
            m.beginTransaction()
                    .replace(R.id.fragment_container, m.findFragmentByTag("mainFragment"))
                    .commit();
        }

    }

}
