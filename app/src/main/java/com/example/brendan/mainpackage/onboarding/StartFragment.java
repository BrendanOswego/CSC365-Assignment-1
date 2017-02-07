package com.example.brendan.mainpackage.onboarding;


import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import com.example.brendan.mainpackage.BaseFragment;

import com.example.brendan.mainpackage.MainActivity;
import com.example.brendan.mainpackage.R;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class StartFragment extends BaseFragment {
    private static final String TAG = StartFragment.class.getName();

    @BindView(R.id.btn_start)
    Button startButton;

    @BindView(R.id.calendar_start)
    CalendarView startCalendar;

    Unbinder unbinder;
    String daySelected;
    String monthSelected;
    String yearSelected;
    String concat;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_start, container, false);
        unbinder = ButterKnife.bind(this, view);
        startButton.setEnabled(false);
        startButton.setBackgroundColor(getResources().getColor(R.color.disabled));
        startCalendar.setOnDateChangeListener(dateListener);
        startCalendar.setDate(System.currentTimeMillis(),false,true);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder = null;
    }


    CalendarView.OnDateChangeListener dateListener = new CalendarView.OnDateChangeListener() {
        @Override
        public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
            startButton.setEnabled(true);
            startButton.setBackgroundColor(getResources().getColor(R.color.enabled));
            daySelected = String.valueOf(day);
            monthSelected = String.valueOf(month);
            yearSelected = String.valueOf(year);
            concat = yearSelected+ "-" + monthSelected+ "-" + daySelected;
            System.out.println(concat);
        }

    };

    @OnClick(R.id.btn_start)
    public void contClicked(){

        ((MainActivity)getActivity()).navigatToEndDate();
    }

}
