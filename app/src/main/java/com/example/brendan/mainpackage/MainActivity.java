package com.example.brendan.mainpackage;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


//REMINDER handle overflow of unnecessary information, check if count or limit is smaller

public class MainActivity extends FragmentActivity {
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseFragment f  = new MainFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        trans.add(R.id.fragment_container,f);
        trans.addToBackStack(null);
        trans.commit();
        //TODO: Add functionality with loadFactor, right now it does nothing.
        CustomHashTable table = new CustomHashTable(.75f);
        table.insert("CAT");
        table.insert("ACT");
        int test1 = table.search("CAT");
        int test2 = table.search("ACT");
        System.out.println("Location: " + test1);
        System.out.println("Location: " + test2);
    }

}
