package com.example.brendan.mainpackage;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.example.brendan.mainpackage.event.DataEvent;
import com.example.brendan.mainpackage.event.StartEvent;
import com.example.brendan.mainpackage.model.DSModel;
import com.example.brendan.mainpackage.model.DataEntries;
import com.example.brendan.mainpackage.model.DayEntries;
import com.example.brendan.mainpackage.model.DayModel;
import com.example.brendan.mainpackage.model.JsonModel;
import com.example.brendan.mainpackage.model.LocationModel;
import com.example.brendan.mainpackage.onboarding.StartFragment;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

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
    private static final String masterJson = "data_2.json";
    private static final String locationJson = "location_model_1.json";
    private String startTime;
    private JsonModel master;

    enum EntryStatus {
        ADDED,
        EXISTS,
        NEW_FILE
    }

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
     * Receives startTime from StartFragment used for information in MainFragment
     *
     * @param event EventBus CallBack event after post has been made
     */
    @Subscribe
    public void onStartEvent(StartEvent event) {
        startTime = event.getTime();
        System.out.println("Listened for StartEvent");
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
     *
     * @param model Model returned from API call.
     * @return Whether or not the Location File already exists
     * @throws IOException
     */
    public boolean writeLocationInternal(LocationModel model) throws IOException {
        File dir = getFilesDir();
        File file = new File(dir, locationJson);
        Gson gson = new Gson();
        Writer writer;
        if (file.exists()) {
            return false;
        } else {
            writer = new FileWriter(file.getAbsolutePath());
            gson.toJson(model, writer);
            writer.flush();
            writer.close();
            return true;
        }

    }

    /**
     *
     * @param newEntry Entry to be added to the Data File containing all Key Values for Hash-Based Cache
     * @param date String for selected day used to check if the Data File contains the information already
     * @return Status of the added entry, i.e whether or not it was added or the entry already exists,
     * or needed to create a new File
     * @throws IOException
     */
    public EntryStatus writeKeyValueData(DayEntries newEntry, String date) throws IOException {
        File dir = getFilesDir();
        File file = new File(dir, masterJson);
        Gson gson = new Gson();
        JsonModel master;
        Writer writer = null;
        BufferedWriter buffer = null;
        if (!file.exists()) {
            try {
                master = new JsonModel();
                ArrayList<DayEntries> temp_day = new ArrayList<>();
                ArrayList<DataEntries> temp_data;
                temp_day.add(newEntry);
                temp_data = newEntry.getDataEntries();
                master.setDays(temp_day);
                master.getDays().get(0).setDataEntries(temp_data);
                writer = new FileWriter(file.getAbsolutePath());
                buffer = new BufferedWriter(writer);
                gson.toJson(master, buffer);
                readData();
            } finally {
                if (buffer != null) buffer.close();
                if (writer != null) writer.close();
            }
            return EntryStatus.NEW_FILE;
        } else {
            try {
                if (entryExists(date)) {
                    readData();
                    return EntryStatus.EXISTS;
                }
                master = getMaster(masterJson);
                master.getDays().add(newEntry);
                int size = master.getDays().size() - 1;
                master.getDays().get(size).setDate(date);
                writer = new FileWriter(file.getAbsolutePath());
                buffer = new BufferedWriter(writer);
                gson.toJson(master, buffer);
                readData();
            } finally {
                if (buffer != null) buffer.close();
                if (writer != null) writer.close();
            }
            return EntryStatus.ADDED;
        }
    }

    /**
     * Reads the Data File containing all Key Values to stdout
     * @throws FileNotFoundException
     */
    private void readData() throws FileNotFoundException {
        InputStream is = openFileInput(masterJson);
        Gson gson = new Gson();
        Reader r = new InputStreamReader(is);
        JsonModel dm = gson.fromJson(r, JsonModel.class);
        Log.v(TAG, "Reading content");
        int i, j;
        for (i = 0; i < dm.getDays().size(); i++) {
            System.out.println("----------------");
            System.out.println("Date: " + dm.getDays().get(i).getDate());
            for (j = 0; j < dm.getDays().get(i).getDataEntries().size(); j++) {
                System.out.println("key: " + dm.getDays().get(i).getDataEntries().get(j).getKey());
                System.out.println("value: " + dm.getDays().get(i).getDataEntries().get(j).getValue());
            }
            System.out.println("----------------");
        }
    }

    /**
     * @return startTime class variable
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * For use in debugging
     * @return Whether in developer mode or not
     */
    public boolean isDevMode() {
        return false;
    }

    /**
     *
     * @param name File name to be searched in Files Directory
     * @return the LocationModel after Gson converts to Java Object
     * @throws FileNotFoundException
     */
    public LocationModel readLocationModel(String name) throws FileNotFoundException {
        FileInputStream iStream = null;
        iStream = openFileInput(name);
        InputStreamReader isr = new InputStreamReader(iStream);
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(isr);
        return gson.fromJson(reader, LocationModel.class);
    }

    void setMaster(JsonModel master) {
        this.master = master;
    }

    /**
     *
     * @param name File name to be search in Files Directory
     * @return the JsonModel of the File containing Json information for the Data
     * @throws FileNotFoundException
     */
    JsonModel getMaster(String name) throws FileNotFoundException {
        File dir = getFilesDir();
        File file = new File(dir, masterJson);
        if (!file.exists()) {
            return null;
        }
        Gson gson = new Gson();
        FileInputStream fis = openFileInput(name);
        InputStreamReader isr = new InputStreamReader(fis);
        return gson.fromJson(isr, JsonModel.class);
    }

    /**
     * Checks if data exists for a specified day chosen by the user.
     * @param date Date chosen by User
     * @return Whether or not data exists for specified date
     * @throws FileNotFoundException
     */
    public boolean entryExists(String date) throws FileNotFoundException {
        readData();
        if (getMaster(masterJson) == null) {
            return false;
        }
        JsonModel model = getMaster(masterJson);
        for (int i = 0; i < model.getDays().size(); i++) {
            if (model.getDays().get(i).getDate().equals(date)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param date Date chosen by User
     * @return The List of DataEntries for a given date
     * @throws IOException
     */
    public ArrayList<DataEntries> getDataEntries(String date) throws IOException {
        JsonModel model = getMaster(masterJson);
        for (int i = 0; i < model.getDays().size(); i++) {
            if (model.getDays().get(i).getDate().equals(date)) {
                return model.getDays().get(i).getDataEntries();
            }
        }
        return null;
    }
}
