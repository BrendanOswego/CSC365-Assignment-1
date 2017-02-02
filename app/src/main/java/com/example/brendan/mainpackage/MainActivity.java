package com.example.brendan.mainpackage;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;


//REMINDER handle overflow of unnecessary information, check if count or limit is smaller

public class MainActivity extends FragmentActivity {
    private static final String TAG = MainActivity.class.getName();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseFragment f  = new MainFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        trans.replace(R.id.fragment_container,f);
        trans.addToBackStack(null);
        trans.commit();

    }

}
