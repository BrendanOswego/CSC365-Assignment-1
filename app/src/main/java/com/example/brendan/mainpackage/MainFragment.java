package com.example.brendan.mainpackage;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brendan.mainpackage.datastrctures.BTree;
import com.example.brendan.mainpackage.datastrctures.CustomHashTable;
import com.example.brendan.mainpackage.api.APIClass;
import com.example.brendan.mainpackage.datastrctures.HashEntry;
import com.example.brendan.mainpackage.datastrctures.Node;
import com.example.brendan.mainpackage.event.DataEvent;
import com.example.brendan.mainpackage.event.FinishEvent;
import com.example.brendan.mainpackage.event.LocationEvent;
import com.example.brendan.mainpackage.model.DataEntries;
import com.example.brendan.mainpackage.model.DataModel;
import com.example.brendan.mainpackage.model.DataResults;
import com.example.brendan.mainpackage.model.DayEntries;
import com.example.brendan.mainpackage.model.LocationModel;
import com.example.brendan.mainpackage.model.Metadata;
import com.example.brendan.mainpackage.model.Result;
import com.example.brendan.mainpackage.view.TempAdapter;
import com.example.brendan.mainpackage.view.TempItem;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Fragment class for making API calls, using EventBus to listen for APIClass Event posts, and
 * using CustomHashTable class for Assignment.
 */
public class MainFragment extends BaseFragment {
    private static final String TAG = MainFragment.class.getName();
    private static final String locationJson = "location_model_1.json";
    @BindView(R.id.tv_date_picked)
    TextView datePicked;
    @BindView(R.id.frame_main)
    FrameLayout mainFrame;
    @BindView(R.id.view_new_search)
    View viewNewSearch;
    @BindView(R.id.listview_main)
    ListView listView;

    Unbinder unbinder;

    private UUID locationUUID;
    private UUID dataResultsUUID;
    private CustomHashTable<String, String> locationTable;
    private CustomHashTable<String, Double> table;
    private CustomHashTable<String, Double> dataHash;
    private List<DataResults> dataResults;
    private EastCoastList ecList = new EastCoastList();
    private ArrayList<String> stateList;
    private int maxData;
    private int globalIndex = 0;
    private String startTime;
    private String endTime;
    private List<String> fipsList;
    private ArrayList<TempItem> listItems;
    private TempAdapter adapter;
    private APIClass api;

    private boolean callMade = false;
    DayEntries newEntry;
    ArrayList<DataEntries> dataList;

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
        newEntry = new DayEntries();
        dataList = new ArrayList<>();
        if (savedInstanceState == null) {
            mainFrame.setVisibility(View.GONE);
            startTime = ((MainActivity) getActivity()).getStartTime();
            endTime = startTime;
            fipsList = new ArrayList<>();
            locationTable = new CustomHashTable<>();
            table = new CustomHashTable<>();
            dataHash = new CustomHashTable<>();
            api = APIClass.getInstance();
            api.init(getActivity());
            listView.setOnItemClickListener(listViewClickListener);
            listItems = new ArrayList<>();
            File dir = getActivity().getFilesDir();
            File f = new File(dir, locationJson);
            if (f.exists()) {
                api.showDialog("Loading Cached Locations", false);
                try {
                    LocationModel model = ((MainActivity) getActivity()).readLocationModel(locationJson);
                    api.closeDialog();
                    postLocationEvent(model, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                api.showDialog("Loading new Locations", false);
                locationUUID = api.getAllStates();
            }
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

    /**
     * Send User back to beginning calling all necessary Android methods for re-initializing MainFragment.
     */
    @OnClick(R.id.view_new_search)
    public void navigateBeginning() {
        ((MainActivity) getActivity()).navigateToStartDate();
    }

    /**
     * Listens for LocationEvent post from the APIClass.
     *
     * @param event LocationEvent post created after APIClass CallBack from Web Service.
     */
    @Subscribe
    public void onLocationEvent(LocationEvent event) throws IOException {
        String name = String.format(getString(R.string.location_write_format), startTime);
        LocationModel model = event.getLocation();
        if (((MainActivity) getActivity()).writeLocationInternal(model)) {
            if (startTime != null) {
                if (event.getUuid().equals(this.locationUUID)) {
                    postLocationEvent(model, true);
                }
            }
        } else {
            postLocationEvent(model, false);
        }

    }

    /**
     * Listens for DataEvent post from the APIClass.
     *
     * @param event DataEvent post created after APIClass CallBack from Web Service.
     */
    @Subscribe
    public void onDataEvent(DataEvent event) throws JSONException, IOException {
        if (event.getUuid().equals(this.dataResultsUUID)) {
            DataModel model = event.getDataModel();
            postDataEvent(model, globalIndex);
        }
    }

    /**
     * Listens for FinishEvent post from the FIPSTask.
     *
     * @param event FinishEvent post created after FIPSTask has returned null in doInBackground().
     */
    @Subscribe
    public void onFinishedEvent(FinishEvent event) throws IOException {
        if (event.isFinished()) {
            adapter = new TempAdapter(getContext(), listItems);
            listView.setAdapter(adapter);
            String text = String.format(getResources().getString(R.string.date_picked), startTime);
            datePicked.setText(text);
            mainFrame.setVisibility(View.VISIBLE);
            newEntry.setDataEntries(dataList);
            newEntry.setDate(startTime);
            ((MainActivity) getActivity()).writeKeyValueData(newEntry, startTime);
        }
    }

    private void postLocationEvent(LocationModel model, boolean state) throws IOException {
        if (state) {
            stateList = new ArrayList<>();
            List<Result> locationResults = model.getResults();
            Metadata md_location = model.getMetadata();
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
        } else {
            stateList = new ArrayList<>();
            LocationModel cached_model = ((MainActivity) getActivity()).readLocationModel(locationJson);
            List<Result> locationResults = cached_model.getResults();
            Metadata md_location = cached_model.getMetadata();
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
        }
        if (((MainActivity) getActivity()).entryExists(startTime)) {
            api.showDialog("Loading cached data", false);
            ArrayList<DataEntries> tempList = ((MainActivity) getActivity()).getDataEntries(startTime);
            Log.v(TAG, "SIZE: " + tempList.size());
            //TODO:Populate ListView
            TempItem temp;
            adapter = new TempAdapter(getContext(), listItems);
            listView.setAdapter(adapter);
            for (int l = 0; l < tempList.size(); l++) {
                temp = new TempItem(ecList.getList().get(l), tempList.get(l).getKey(), tempList.get(l).getValue());
                table.insert(tempList.get(l).getKey(), tempList.get(l).getValue());
                listItems.add(temp);
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            String text = String.format(getResources().getString(R.string.date_picked), startTime);
            datePicked.setText(text);
            mainFrame.setVisibility(View.VISIBLE);
            api.closeDialog();
        } else {
            api.showDialog("Searching for Temperatures", true);
            FIPSTask task = new FIPSTask();
            String[] dates = new String[2];
            dates[0] = startTime;
            dates[1] = endTime;
            task.execute(dates);
        }
    }

    private void setGlobalIndex(int index) {
        globalIndex = index;
    }

    /**
     * Adds information to table CustomHashTable (Assignment 1)
     *
     * @param index index from fipsList ArrayList
     */
    private void postDataEvent(DataModel model, int index) throws JSONException, IOException {
        dataResults = model.getResults();
        Metadata md = model.getMetadata();
        if (dataResults != null) {
            int i = md.getResultset().getCount();
            int j = md.getResultset().getLimit();
            maxData = (i <= j) ? i : j;
        }
        double total = 0.0;
        int tempStations = 0;
        TempItem temp;
        if (dataResults != null) {
            for (int i = 0; i < maxData; i++) {
                if (dataResults.get(i).getDatatype().equals("TAVG")) {
                    Double value = dataResults.get(i).getValue();
                    String station = dataResults.get(i).getStation();
                    dataHash.insert(station, value);
                    total += dataResults.get(i).getValue();
                    ++tempStations;
                }
            }
            total = total / tempStations;
            table.insert(fipsList.get(index), total);
            temp = new TempItem(stateList.get(index), fipsList.get(index), total);
        } else {
            temp = new TempItem(stateList.get(index), fipsList.get(index), null);
        }
        listItems.add(temp);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        callMade = true;
        //Grab the entire table and traverse through to find nonempty
        DataEntries dataEntry = new DataEntries();
        dataEntry.setKey(fipsList.get(index));
        dataEntry.setValue(total);
        dataList.add(dataEntry);
    }

    /**
     * Calculates Euclidean Distance of 2 Doubles
     *
     * @param x Double being compared to by parameter y
     * @param y Double to compare to parameter x
     * @return Distance between x and y
     */
    public double getDistance(double x, double y) {
        double sum = 0.0;
        if (x > y) {
            sum += Math.pow(x - y, 2.0);
        } else {
            sum += Math.pow(y - x, 2.0);
        }
        return Math.sqrt(sum);
    }

    AdapterView.OnItemClickListener listViewClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int viewIndex, long l) {
            TextView state = (TextView) view.findViewById(R.id.view_temp_name);
            String fips = locationTable.search(state.getText().toString());
            Double compareTo = table.search(fips);
            if (compareTo != null) {
                Double temp;
                //Playing it safe
                Double winner = 0.0000000000;
                int index = 0;
                for (int i = 0; i < fipsList.size(); i++) {
                    if (table.search(fipsList.get(i)) != null) {
                        temp = table.search(fipsList.get(i));
                        Double distance = getDistance(compareTo, temp);
                        if (((distance < winner) || winner == 0.0000000000) && !temp.equals(compareTo)) {
                            winner = distance;
                            index = i;
                        }
                    }
                }
                Toast.makeText(getContext(), "Most Similar: " + stateList.get(index), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "There is no information for selected state", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * Private inner class for handling  Asynchronous Data API calls since cannot happen on Main Thread, based off of the laws of Android development.
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private class FIPSTask extends AsyncTask<String, Void, Void> {
        private boolean finished = false;

        /**
         * Override method needed for making api calls in a separate thread then reverts back to
         * Main Thread when done
         *
         * @param strings list of strings for the start and end date
         * @return null when done with Async api calls.
         */
        @Override
        protected Void doInBackground(final String... strings) {
            int l = 0;
            while (l < ecList.size()) {
                System.out.println("Background task " + l);
                dataResultsUUID = api.getData("GHCND", "TAVG", fipsList.get(globalIndex), startTime, endTime);
                while (!callMade) {
                    try {
                        //Need to have fun otherwise I'll go crazy
                        if (((MainActivity) getActivity()).isDevMode())
                            System.out.println("Sherlock is sleeping...");
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(200);
                    setGlobalIndex(l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                callMade = false;
                l++;
                if (l < ecList.size()) {
                    globalIndex++;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        api.getDialog().setMessage(fipsList.get(globalIndex));
                        api.getDialog().setProgress(globalIndex);
                    }
                });
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
    }
}
