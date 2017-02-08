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
//TODO: only add spinner items is PRCP value is not null
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
    private ArrayList<String> stateList;

    ArrayList<String> noNullList;
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
            startTime = ((MainActivity)getActivity()).getStartTime();
            endTime = ((MainActivity)getActivity()).getEndTime();
            fipsList = new ArrayList<>();
            locationTable = new CustomHashTable<>();
            table = new CustomHashTable<>();
            api = APIClass.getInstance();
            api.init(getActivity());
            mainFrame.setVisibility(View.GONE);
            locationUUID = api.getAllStates();
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
        unbinder = null;
        table = null;
        locationTable = null;
        EventBus.getDefault().unregister(this);
    }



    @Subscribe
    public void onLocationEvent(LocationEvent event) {
        if(startTime != null && endTime != null) {
            if (event.getUuid().equals(this.locationUUID)) {
                stateList = new ArrayList<>();
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
                api.showDialog("Fetching FIPS");
                task = new FIPSTask();
                //TODO Set the start and end dates from the StartFragment and EndFragment arguments
                String[] dates = new String[2];
                dates[0] = startTime;
                dates[1] = endTime;
                task.execute(dates);
            }
        }

    }

    @Subscribe
    public void onDataEvent(DataEvent event) {
        if (event.getUuid().equals(dataResultsUUID)) {
            dataResults = event.getDataModel().getResults();
            Metadata md = event.getDataModel().getMetadata();
            int i = md.getResultset().getCount();
            int j = md.getResultset().getLimit();
            maxData = (i <= j) ? i : j;
            postDataEvent(globalIndex);
            if (task.isFinished()) {
                noNullList = new ArrayList<>();
                for (int x = 0; x < stateList.size(); x++) {
                    String fips = locationTable.search(stateList.get(x));
                    if (table.search(fips) != null) {
                        noNullList.add(stateList.get(x));
                        double prcp = table.search(fips);
                        System.out.println("FIPS ID: " + fips + "PRCP: " + prcp);
                    }

                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, noNullList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                stateSpinner.setAdapter(dataAdapter);
                stateSpinner.setOnItemSelectedListener(itemListener);
                mainFrame.setVisibility(View.VISIBLE);
            }
        }


    }

    AdapterView.OnItemSelectedListener itemListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int a, long b) {
            String fips = locationTable.search((String) adapterView.getSelectedItem());
            double fipsDouble = table.search(fips);
            double mostSimilar;
            System.out.println("PRCP FOR " + adapterView.getSelectedItem() + " is " + fipsDouble);
//            for(int i = 0;i<noNullList.size();i++){
//                for(int j = i + 1;j<noNullList.size();j++){
//
//                }
//            }
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
        double total = 0;
        int prcpStations = 0;

        for (int i = 0; i < maxData; i++) {
            if (dataResults.get(i).getDatatype().equalsIgnoreCase("PRCP")) {
                total += dataResults.get(i).getValue();
                ++prcpStations;
            }
        }
        total = total / prcpStations;
        table.insert(fipsList.get(fipsIndex), total);
        foo(fipsIndex);
    }

    void foo(int index) {
        double test = table.search(fipsList.get(index));
        System.out.println(fipsList.get(index) + ": " + test);
    }

    private void postLocationEvent(int index, String start, String end) {
        globalIndex = index;
        dataResultsUUID = api.getData("GHCND", fipsList.get(index), start, end);
    }


    private class FIPSTask extends AsyncTask<String, Void, Void> {
        private boolean finished = false;

        @Override
        protected Void doInBackground(String... strings) {
            int l = 0;
            while (l < fipsList.size()) {
                if (l % 5 != 0) {
                    postLocationEvent(l, strings[0], strings[1]);
                } else {
                    try {
                        Thread.sleep(1700);
                        postLocationEvent(l, strings[0], strings[1]);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                l++;
            }
            api.closeDialog();
            finished = true;
            return null;
        }

        boolean isFinished() {
            return finished;
        }

    }

    private double cosineSimilarity(double vec1, double vec2) {
        double dotProduct = 0.0;
        double sum1;
        double sum2;

        dotProduct += vec1 * vec2;
        sum1 = Math.pow(vec1, 2);
        sum2 = Math.pow(vec2, 2);

        return (dotProduct / (Math.sqrt(sum1) * Math.sqrt(sum2)));
    }


}
