package com.example.brendan.mainpackage.onboarding;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import com.example.brendan.mainpackage.BaseFragment;
import com.example.brendan.mainpackage.MainActivity;
import com.example.brendan.mainpackage.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class EndFragment extends BaseFragment {
    private static final String TAG = EndFragment.class.getName();
    @BindView(R.id.btn_end)
    Button endButton;

    @BindView(R.id.calendar_end)
    CalendarView endCalendar;

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
        final View view = inflater.inflate(R.layout.fragment_end, container, false);
        unbinder = ButterKnife.bind(this,view);
        endButton.setEnabled(false);
        endButton.setBackgroundColor(getResources().getColor(R.color.disabled));
        endCalendar.setOnDateChangeListener(dateListener);
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
        public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
            daySelected = String.valueOf(day);
            monthSelected = String.valueOf(month);
            endButton.setEnabled(true);
            endButton.setBackgroundColor(getResources().getColor(R.color.enabled));
            yearSelected = String.valueOf(year);
            concat = yearSelected+ "-" + monthSelected+ "-" + daySelected;
            Log.v(TAG,concat);
        }
    };

    @OnClick(R.id.btn_end)
    public void contClicked(){
        ((MainActivity)getActivity()).navigateToMain();
    }
}
