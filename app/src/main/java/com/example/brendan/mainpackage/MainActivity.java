package com.example.brendan.mainpackage;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.example.brendan.mainpackage.event.EndEvent;
import com.example.brendan.mainpackage.event.StartEvent;
import com.example.brendan.mainpackage.onboarding.EndFragment;
import com.example.brendan.mainpackage.onboarding.MainFragment;
import com.example.brendan.mainpackage.onboarding.StartFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getName();

    private String startTime;
    private String endTime;
    private int startMonth;
    private int startDay;
    private int startYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
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

    @Subscribe
    public void onStartEvent(StartEvent event) {
        startTime = event.getTime();
        System.out.println("Listened for StartEvent");
    }

    @Subscribe
    public void onEndEvent(EndEvent event) {
        endTime = event.getTime();
        System.out.println("Listened for EndEvent");
    }


    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public int getStartDay() {
        return startDay;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }
}
