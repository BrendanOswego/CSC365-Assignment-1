package com.example.brendan.mainpackage;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

//REMINDER handle overflow of unnecessary information, check if count or limit is smaller

public class MainActivity extends FragmentActivity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO: Add functionality with loadFactor, right now it does nothing.
    }

}
