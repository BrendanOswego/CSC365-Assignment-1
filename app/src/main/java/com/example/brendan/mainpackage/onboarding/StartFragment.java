package com.example.brendan.mainpackage.onboarding;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import com.example.brendan.mainpackage.BaseFragment;
import com.example.brendan.mainpackage.MainActivity;
import com.example.brendan.mainpackage.R;
import com.example.brendan.mainpackage.event.StartEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Fragment class for handling the start date chosen for the MainFragment API calls.
 */
public class StartFragment extends BaseFragment {
    private static final String TAG = BaseFragment.class.getName();
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
        startCalendar.setDate(System.currentTimeMillis(), false, true);
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
        @SuppressLint("DefaultLocale")
        @Override
        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int currentMonth = cal.get(Calendar.MONTH);
            int currentDay = cal.get(Calendar.DAY_OF_MONTH);
            int currentYear = cal.get(Calendar.YEAR);
            if(currentYear >= year){
                if((year < currentYear) || ((currentMonth == month) && (currentDay >= day))
                        || (month < currentMonth)){
                    startButton.setEnabled(true);
                    startButton.setBackgroundColor(getResources().getColor(R.color.enabled));
                    if (month <= 10) {
                        monthSelected = String.format("%02d", month + 1);
                    } else {
                        monthSelected = String.valueOf(month + 1);
                    }
                    if (day < 10) {
                        daySelected = String.format("%02d", day);
                    } else {
                        daySelected = String.valueOf(day);
                    }
                    yearSelected = String.valueOf(year);
                    concat = yearSelected + "-" + monthSelected + "-" + daySelected;
                } else {
                    startButton.setEnabled(false);
                    startButton.setBackgroundColor(getResources().getColor(R.color.disabled));
                }
            } else {
                startButton.setEnabled(false);
                startButton.setBackgroundColor(getResources().getColor(R.color.disabled));
            }

        }


    };

    /**
     * Sends User to MainFragment when necessary information is chosen from the CalendarView
     */
    @OnClick(R.id.btn_start)
    public void submitClicked() {
        EventBus.getDefault().post(new StartEvent(concat));
        ((MainActivity) getActivity()).navigateToMain();
    }

}
