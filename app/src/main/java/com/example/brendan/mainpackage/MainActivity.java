package com.example.brendan.mainpackage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.example.brendan.mainpackage.event.LocationEvent;
import com.example.brendan.mainpackage.event.StartEvent;
import com.example.brendan.mainpackage.model.DataModel;
import com.example.brendan.mainpackage.model.LocationModel;
import com.example.brendan.mainpackage.onboarding.MainFragment;
import com.example.brendan.mainpackage.onboarding.StartFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

/**
 * Main Activity that handles Fragment Navigation and the StartEvent event.
 * - This appilcation takes in weather data from the NOAA API https://www.ncdc.noaa.gov/cdo-web/webservices/v2.
 * - Specific data for the program consists of taking the Mean Average Temperature from the East Coast
 * States via a FIPS ID and stores them into a CustomHashTable<K,V> class.
 * - The MainFragment handles the API calls for getting the Location FIPS ID's as well as the calls
 * for getting the Temperature data.
 * - Once Retrofit successfully receives a response from the web service it makes an EventBus post
 * that the MainFragment listens for.
 * - Once the MainFragment receives a post it adds the response body to the respective CustomHashTable table.
 * - When all calls are made a ListView is populated by TempItems and listens for clicks to elements
 * - When an element is clicked a similarity metric returns the temperature that is closest to the
 * element that was clicked
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getName();
    private String startTime;

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

    /**
     * Sends User to StartFragment
     */
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

    /**
     * Sends User to MainFragment
     */
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

    /**
     * Receives startTime from StartFragment used for information in MainFragment
     *
     * @param event EventBus CallBack event after post has been made
     */
    @Subscribe
    public void onStartEvent(StartEvent event) {
        startTime = event.getTime();
        System.out.println("Listened for StartEvent");
    }

    public void writeDataInternal(DataModel model, String name) {
        File dir = getCacheDir();
        int count = dir.listFiles().length;
        count = count + 1;
        File file = new File(dir, "data_model_" + count);
        Gson gson = new Gson();
        String content = gson.toJson(model);
    }

    public void writeLocationInternal(LocationEvent event, String name) {
        File dir = getFilesDir();
        Log.v(TAG,"Files Directory: " + dir.getAbsolutePath());
        File file = new File(dir, name);
        Gson gson = new Gson();
        String content = gson.toJson(event.getLocation());
        Log.v(TAG, content);
        Log.v(TAG,file.getAbsolutePath());
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(name, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
            Log.v(TAG,"Data saved to " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    /**
     * @return startTime class variable
     */
    public String getStartTime() {
        return startTime;
    }

    public boolean isDevMode() {
        return false;
    }

    public boolean fileExists(File f) {
        File dir = getFilesDir();
        if (dir.exists()) {
            for (File temp : dir.listFiles()) {
                if (temp.equals(f)) {
                    Log.v(TAG, "Temp file equals f");
                    return true;
                }
            }
        } else {
            Log.v(TAG,"cacheDir does not exist");
        }
        Log.v(TAG,"fileExists return false");
        return false;
    }


    public void readLocationData(String name) throws IOException {
        //TODO: Return LocationEvent object given the path name
    }
}
