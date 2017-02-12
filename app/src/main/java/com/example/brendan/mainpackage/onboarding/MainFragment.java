package com.example.brendan.mainpackage.onboarding;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.brendan.mainpackage.BaseFragment;
import com.example.brendan.mainpackage.CustomHashTable;
import com.example.brendan.mainpackage.EastCoastList;
import com.example.brendan.mainpackage.MainActivity;
import com.example.brendan.mainpackage.R;
import com.example.brendan.mainpackage.api.APIClass;
import com.example.brendan.mainpackage.event.DataEvent;
import com.example.brendan.mainpackage.event.EndEvent;
import com.example.brendan.mainpackage.event.FinishEvent;
import com.example.brendan.mainpackage.event.LocationEvent;
import com.example.brendan.mainpackage.event.StartEvent;
import com.example.brendan.mainpackage.model.DataResults;
import com.example.brendan.mainpackage.model.LocationModel;
import com.example.brendan.mainpackage.model.Metadata;
import com.example.brendan.mainpackage.model.Result;
import com.example.brendan.mainpackage.view.TempAdapter;
import com.example.brendan.mainpackage.view.TempItem;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Fragment class for making API calls, using EventBus to listen for APIClass Event posts.
 * Adding State,Precipitation pairs.
 * PRCP is in hundredths of an inch
 */
//TODO Check data from time frame and see if null. If it is, remove it from the spinner
public class MainFragment extends BaseFragment {
    private static final String TAG = MainFragment.class.getName();

    @BindView(R.id.spinner_states)
    Spinner stateSpinner;
    @BindView(R.id.frame_main)
    FrameLayout mainFrame;

    @BindView(R.id.view_new_search)
    View viewNewSearch;

    @BindView(R.id.listview_main)
    ListView listView;

    Unbinder unbinder;

    private UUID locationUUID;
    private UUID dataResultsUUID;
    //Key:State Value:FIPS
    private CustomHashTable<String, String> locationTable;
    //Key:FIPS Value:PRCP
    private CustomHashTable<String, Double> table;
    private List<DataResults> dataResults;
    EastCoastList ecList = new EastCoastList();
    private ArrayList<String> stateList;
    private ArrayList<Integer> removeList;
    private int maxData;
    private int globalIndex = 0;
    private String startTime;
    private String endTime;
    private List<String> fipsList;
    private FIPSTask task;

    private APIClass api;

    File locationFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (savedInstanceState == null) {
            locationFile = new File(getContext().getFilesDir(), "locationFile.json");
            System.out.println("SavedInstanceState is NULL");
            startTime = ((MainActivity) getActivity()).getStartTime();
            endTime = ((MainActivity) getActivity()).getEndTime();
            fipsList = new ArrayList<>();
            locationTable = new CustomHashTable<>();
            table = new CustomHashTable<>();
            api = APIClass.getInstance();
            api.init(getActivity());
            locationUUID = api.getAllStates();
        } else {
            System.out.println("SavedInstanceState is NOT NULL PROBLEM");
        }
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
        System.out.println("Calling OnDestroy");
        unbinder = null;
        table = null;
        locationTable = null;
        EventBus.getDefault().unregister(this);
    }

    private void writeToFile(LocationModel locationModel) {
        System.out.println("Writing to file");
        String fileName = "locationFile.json";
        FileOutputStream outputStream;
        Gson gson = new Gson();
        gson.toJson(locationModel);
        try {
            outputStream = getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(gson.toString().getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(gson.toJson(locationModel));
    }

    @Subscribe
    public void onLocationEvent(LocationEvent event) {
        if (startTime != null) {
            if (event.getUuid().equals(this.locationUUID)) {
                removeList = new ArrayList<>();
                stateList = new ArrayList<>();
                List<Result> locationResults = event.getLocation().getResults();
                Metadata md_location = event.getLocation().getMetadata();
                writeToFile(event.getLocation());
                int i = md_location.getResultset().getCount();
                int j = md_location.getResultset().getLimit();
                int count = (i <= j) ? i : j;
                for (int k = 0; k < count; k++) {
                    if (!fipsList.contains(locationResults.get(k).getId())) {
                        for (int x = 0; x < ecList.size(); x++) {
                            if (ecList.getList().get(x).equals(locationResults.get(k).getName())) {
                                fipsList.add(locationResults.get(k).getId());
                                stateList.add(locationResults.get(k).getName());
                                locationTable.insert(locationResults.get(k).getName(), locationResults.get(k).getId());
                            }
                        }
                    }
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, stateList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                stateSpinner.setAdapter(dataAdapter);
                stateSpinner.setOnItemSelectedListener(itemListener);
            }
        }

    }

    @Subscribe
    public void onDataEvent(DataEvent event) {
        if (event.getUuid().equals(this.dataResultsUUID)) {
            dataResults = event.getDataModel().getResults();
            Metadata md = event.getDataModel().getMetadata();
            if (dataResults != null) {
                int i = md.getResultset().getCount();
                int j = md.getResultset().getLimit();
                maxData = (i <= j) ? i : j;
                double total = 0;
                int prcpStations = 0;
                for (int y = 0; y < maxData; y++) {
                    if (dataResults.get(y).getDatatype().equals("TAVG")) {
                        total += dataResults.get(y).getValue();
                        ++prcpStations;
                    }
                }
                total = total / prcpStations;
                table.insert(fipsList.get(globalIndex), total);
                postDataEvent(globalIndex);

            } else {
                removeList.add(globalIndex);
                System.out.println("DATA RESULT NULL");
            }
        }
    }

    @Subscribe
    public void onFinishedEvent(FinishEvent event) {
        if (event.isFinished()) {
            String fips = locationTable.search((String) stateSpinner.getSelectedItem());
            Double value = table.search(fips);
            if (value != null) {
                Toast.makeText(getContext(), String.valueOf(value), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "No information for " + fips, Toast.LENGTH_SHORT).show();
            }
            ArrayList<TempItem> listItems = new ArrayList<>();
            TempAdapter adapter = new TempAdapter(getContext(), listItems);
            listView.setAdapter(adapter);
            for (int i = 0; i < stateList.size(); i++) {
                String name = stateList.get(i);
                String fip = locationTable.search(name);
                Double val = table.search(fip);
                TempItem temp = new TempItem(name, fip, val);
                adapter.add(temp);
            }
        }
    }

    AdapterView.OnItemSelectedListener itemListener = new AdapterView.OnItemSelectedListener() {
        int count = 0;

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int a, long b) {
            if (count >= 1) {
                api.showDialog("Fetching Information");
                task = new FIPSTask();
                String[] dates = new String[2];
                dates[0] = startTime;
                dates[1] = startTime;
                task.setIndex(adapterView.getSelectedItemPosition());
                task.execute(dates);

            }
            count++;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    @OnClick(R.id.view_new_search)
    public void navigateBeginning() {
        ((MainActivity) getActivity()).navigateToStartDate();
    }


    private void postDataEvent(int fipsIndex) {
        System.out.println("Calling POST DATA EVENT");
        double total = 0;
        int prcpStations = 0;
        for (int i = 0; i < maxData; i++) {
            if (dataResults.get(i).getDatatype().equals("TAVG")) {
                total += dataResults.get(i).getValue();
                ++prcpStations;
            }
        }
        total = total / prcpStations;
        System.out.println("Total for index " + fipsIndex + "==" + total);
        table.insert(fipsList.get(fipsIndex), total);

        if (fipsIndex == stateSpinner.getSelectedItemPosition()) {
            Toast.makeText(getContext(), "Total for selected state: " + total, Toast.LENGTH_SHORT).show();
        }

    }

    private void postLocationEvent(int index, String start, String end) {
        globalIndex = index;
        dataResultsUUID = api.getData("GHCND", "TAVG", fipsList.get(index), start, end);
    }


    private class FIPSTask extends AsyncTask<String, Void, Void> {
        private boolean finished = false;
        int index = 0;

        @Override
        protected Void doInBackground(final String... strings) {
            int l = 0;
            while (l < ecList.size()) {
                if (l % 5 == 0) {
                    postLocationEvent(l, strings[0], strings[1]);
                    try {
                        Thread.sleep(1200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    postLocationEvent(l, strings[0], strings[1]);
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                l++;
            }
            finished = true;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new FinishEvent(finished));
                }
            });

            api.closeDialog();
            return null;
        }

        boolean isFinished() {
            return finished;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

    }
}
