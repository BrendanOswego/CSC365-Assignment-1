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
import com.example.brendan.mainpackage.event.EndEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//TODO Take selected date and take as Bundle argument in MainFragment
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
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_end, container, false);
        unbinder = ButterKnife.bind(this, view);
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
        @SuppressLint("DefaultLocale")
        @Override
        public void onSelectedDayChange(@NonNull CalendarView view, int selectedYear, int selectedMonth, int selectedDay) {
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int currentMonth = cal.get(Calendar.MONTH);
            int currentDay = cal.get(Calendar.DAY_OF_MONTH);
            int currentYear = cal.get(Calendar.YEAR);
            int startDay = ((MainActivity) getActivity()).getStartDay();
            int startMonth = ((MainActivity) getActivity()).getStartMonth();
            int startYear = ((MainActivity) getActivity()).getStartYear();
//            System.out.println("Start Month: " + startMonth);
//            System.out.println("Selected Month(+1): " + (selectedMonth + 1));
//            System.out.println("Selected Year: " + selectedYear);

            if (currentYear >= selectedYear) {
                if ((selectedYear < currentYear)
                        || ((currentMonth == selectedMonth)
                        && (currentDay >= selectedDay))
                        || (selectedMonth < currentMonth)) {
                    if (startMonth <= (selectedMonth + 1)
                            || (startYear < selectedYear)) {
                        System.out.println("True for month");
                        if (((startDay <= selectedDay)
                                && (startYear <= selectedYear))
                                || ((currentYear >= startYear)
                                && (startMonth <= selectedMonth))
                                || (selectedYear > startYear)) {
                            endButton.setEnabled(true);
                            endButton.setBackgroundColor(getResources().getColor(R.color.enabled));
                            if (selectedMonth <= 10) {
                                monthSelected = String.format("%02d", selectedMonth + 1);
                            } else {
                                monthSelected = String.valueOf(selectedMonth + 1);
                            }
                            if (selectedDay < 10) {
                                daySelected = String.format("%02d", selectedDay);
                            } else {
                                daySelected = String.valueOf(selectedDay);
                            }
                            yearSelected = String.valueOf(selectedYear);
                            concat = yearSelected + "-" + monthSelected + "-" + daySelected;
                        } else {
                            endButton.setEnabled(false);
                            endButton.setBackgroundColor(getResources().getColor(R.color.disabled));
                        }
                    } else {
                        endButton.setEnabled(false);
                        endButton.setBackgroundColor(getResources().getColor(R.color.disabled));
                    }

                } else {
                    endButton.setEnabled(false);
                    endButton.setBackgroundColor(getResources().getColor(R.color.disabled));
                }
            } else {
                endButton.setEnabled(false);
                endButton.setBackgroundColor(getResources().getColor(R.color.disabled));
            }
        }
    };

    @OnClick(R.id.btn_end)
    public void contClicked() {
        EventBus.getDefault().post(new EndEvent(concat));
        ((MainActivity) getActivity()).navigateToMain();
    }
}
