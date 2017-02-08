package com.example.brendan.mainpackage.onboarding;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.brendan.mainpackage.BaseFragment;
import com.example.brendan.mainpackage.CustomHashTable;
import com.example.brendan.mainpackage.MainActivity;
import com.example.brendan.mainpackage.R;
import com.example.brendan.mainpackage.api.APIClass;
import com.example.brendan.mainpackage.event.DataEvent;
import com.example.brendan.mainpackage.event.EndEvent;
import com.example.brendan.mainpackage.event.LocationEvent;
import com.example.brendan.mainpackage.event.StartEvent;
import com.example.brendan.mainpackage.model.DataResults;
import com.example.brendan.mainpackage.model.Metadata;
import com.example.brendan.mainpackage.model.Result;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    Unbinder unbinder;

    private UUID locationUUID;
    private UUID dataResultsUUID;
    //Key:State Value:FIPS
    private CustomHashTable<String, String> locationTable;
    //Key:FIPS Value:PRCP
    private CustomHashTable<String, Double> table;
    private List<DataResults> dataResults;

    private int maxData;
    private int globalIndex = 0;
    private String startTime;
    private String endTime;
    private List<String> fipsList;
    private FIPSTask task;

    private APIClass api;

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


    @Subscribe
    public void onLocationEvent(LocationEvent event) {
        if (startTime != null) {
            if (event.getUuid().equals(this.locationUUID)) {
                ArrayList<String> stateList = new ArrayList<>();
                List<Result> locationResults = event.getLocation().getResults();
                Metadata md_location = event.getLocation().getMetadata();
                int i = md_location.getResultset().getCount();
                int j = md_location.getResultset().getLimit();
                int count = (i <= j) ? i : j;
                for (int k = 0; k < count; k++) {
                    if (!fipsList.contains(locationResults.get(k).getId())) {
                        fipsList.add(locationResults.get(k).getId());
                        stateList.add(locationResults.get(k).getName());
                        locationTable.insert(locationResults.get(k).getName(), locationResults.get(k).getId());
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
                    if (dataResults.get(y).getDatatype().equals("PRCP")) {
                        total += dataResults.get(y).getValue();
                        System.out.println("TOTAL IN LOOP: " + total);
                        ++prcpStations;
                    }
                }
                total = total / prcpStations;
                table.insert(fipsList.get(globalIndex), total);
                Toast.makeText(getContext(), "Total: " + total, Toast.LENGTH_SHORT).show();

                if (task.isFinished()) {
                    postDataEvent(task.getIndex());
                }
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
            if (dataResults.get(i).getDatatype().equals("PRCP")) {
                total += dataResults.get(i).getValue();
                ++prcpStations;
            }
        }
        total = total / prcpStations;
        System.out.println("Total for index " + fipsIndex + "==" + total);
        table.insert(fipsList.get(fipsIndex), total);

    }

    private void postLocationEvent(int index, String start, String end) {
        globalIndex = index;
        dataResultsUUID = api.getData("GHCND", fipsList.get(index), start, end);
    }


    private class FIPSTask extends AsyncTask<String, Void, Void> {
        private boolean finished = false;
        int index = 0;

        @Override
        protected Void doInBackground(String... strings) {
            postLocationEvent(index, strings[0], strings[1]);
            api.closeDialog();
            finished = true;
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
