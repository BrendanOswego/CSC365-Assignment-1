package com.example.brendan.mainpackage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.brendan.mainpackage.api.APIClass;
import com.example.brendan.mainpackage.datastrctures.BTree;
import com.example.brendan.mainpackage.datastrctures.CustomHashTable;
import com.example.brendan.mainpackage.datastrctures.Node;
import com.example.brendan.mainpackage.event.CityDataEvent;
import com.example.brendan.mainpackage.event.CityLocationEvent;
import com.example.brendan.mainpackage.event.FinishEvent;
import com.example.brendan.mainpackage.model.CityJson;
import com.example.brendan.mainpackage.model.DataModel;
import com.example.brendan.mainpackage.model.DataResults;
import com.example.brendan.mainpackage.model.LocationModel;
import com.example.brendan.mainpackage.view.CityAdapter;
import com.example.brendan.mainpackage.view.CityItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.GONE;

/**
 * Created by brendan on 3/29/17.
 */

public class CityFragment extends BaseFragment {
    private static final String TAG = CityFragment.class.getName();
    private static final String allLocationsJson = "all_locations.json";
    private static final String summerValuesRAF = "summer_values.dat";
    private static final String winterValuesRAF = "winter_values.dat";
    private static final String summerModel = "summer_model.json";
    private static final String winterModel = "winterModel.json";
    private static final String btreeFile = "btree.dat";
    private static final String SET_1 = "TAVG";
    private static final String SET_2 = "TMAX";

    @BindView(R.id.listview_city)
    ListView listView;
    @BindView(R.id.btn_nav_selection)
    Button navSelection;
    @BindView(R.id.btn_summer)
    Button summer;
    @BindView(R.id.btn_winter)
    Button winter;
    @BindView(R.id.frame_city)
    FrameLayout mainFrame;

    Unbinder unbinder;

    UUID locationUUID;
    UUID dataUUID;
    APIClass api;

    CityAdapter adapter;

    RandomAccessFile dataRAF;
    RandomAccessFile btreeRAF;

    private boolean callMade = false;
    private int globalIndex = 0;
    String summerTime = "2016-07-15";
    String winterTime = "2017-01-15"; //Year-Month-Day
    String date;
    ArrayList<String> cityNameList;
    ArrayList<String> cityIDList;
    ArrayList<Double> set_1_list;
    ArrayList<Double> set_2_list;
    ArrayList<DataResults> dataResults;
    ArrayList<CityItem> cityItemList;
    String dataSet;
    String name;
    String url;
    CustomHashTable<String, String> nameIDTable;
    int max = 1000;

    BTree tree;
    String seasonModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_city, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (savedInstanceState == null) {
            cityNameList = new ArrayList<>();
            cityIDList = new ArrayList<>();
            set_1_list = new ArrayList<>();
            set_2_list = new ArrayList<>();
            cityItemList = new ArrayList<>();
            nameIDTable = new CustomHashTable<>();
            try {
                tree = new BTree(6, getContext());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            mainFrame.setVisibility(View.VISIBLE);
            api = APIClass.getInstance();
            api.init(getActivity());
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
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.btn_nav_selection)
    public void navigateToSelection() {
        ((MainActivity) getActivity()).navigateToSelection();
    }

    @OnClick(R.id.btn_winter)
    public void loadWinterData() {
        seasonModel = winterModel;
        if (adapter != null) {
            cityItemList = new ArrayList<>();
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
        date = winterTime;
        mainFrame.setVisibility(GONE);
        File dir = getActivity().getFilesDir();
        File f = new File(dir, allLocationsJson);
        if (!f.exists()) {
            api.showDialog("Loading New Locations", false, null);
            locationUUID = api.getAllLocations();
        } else {
            api.showDialog("Loading Cached Locations", false, null);
            LocationModel model = null;
            try {
                model = ((MainActivity) getActivity()).getLocationModel();
                postLocationEvent(model, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.btn_summer)
    public void loadSummerData() {
        seasonModel = summerModel;
        if (adapter != null) {
            cityItemList = new ArrayList<>();
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
        date = summerTime;
        mainFrame.setVisibility(GONE);
        File dir = getActivity().getFilesDir();
        File f = new File(dir, allLocationsJson);
        if (!f.exists()) {
            api.showDialog("Loading New Locations", false, null);
            locationUUID = api.getAllLocations();
        } else {
            api.showDialog("Loading Cached Locations", false, null);
            LocationModel model = null;
            try {
                model = ((MainActivity) getActivity()).getLocationModel();
                postLocationEvent(model, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe
    public void onLocationEvent(CityLocationEvent event) throws IOException {
        if (event.getUuid().equals(this.locationUUID)) {
            LocationModel model = event.getModel();
            postLocationEvent(model, false);
        }
    }

    @Subscribe
    public void onDataEvent(CityDataEvent event) throws IOException {
        if (event.getUuid().equals(dataUUID)) {
            MainActivity.CityStatus status = ((MainActivity) getActivity()).writeCityFile(event.getUrl(), event.getMode(), seasonModel);
            Log.v(TAG, status.toString());
            postDataEvent(event.getMode(), globalIndex, dataSet, event.getUrl());
        }
    }

    @Subscribe
    public void onFinishedEvent(FinishEvent event) throws IOException, ClassNotFoundException {
        if (!event.isCached()) {
            if (event.getUuid().equals(dataUUID)) {
                if (event.getSetType().equals(SET_1)) {
                    globalIndex = 0;
                    String[] params = new String[2];
                    params[0] = date;
                    params[1] = SET_2;
                    api.closeDialog();
                    api.showDialog("Loading TMAX", true, max);
                    new Task().execute(params);
                } else if (event.getSetType().equals(SET_2)) {
                    if (event.isFinished()) {
                        CityItem item;
                        File f;
                        if (date.equals(summerTime)) {
                            f = new File(getActivity().getFilesDir(), summerValuesRAF);
                        } else {
                            f = new File(getActivity().getFilesDir(), winterValuesRAF);
                        }
                        dataRAF = new RandomAccessFile(f, "rw");
                        for (int i = 0; i < cityNameList.size(); i++) {
                            item = new CityItem(cityNameList.get(i), set_1_list.get(i), set_2_list.get(i));
                            dataRAF.writeBytes(cityIDList.get(i));
                            if (set_1_list.get(i) != null) {
                                dataRAF.writeDouble(set_1_list.get(i));
                            } else {
                                dataRAF.writeDouble(-999);
                            }
                            if (set_2_list.get(i) != null) {
                                dataRAF.writeDouble(set_2_list.get(i));
                            } else {
                                dataRAF.writeDouble(-999);
                            }
                            cityItemList.add(item);
                        }
                        dataRAF.seek(0);
                        for (int j = 0; j < cityNameList.size(); j++) {
                            byte[] buffer = new byte[cityIDList.get(j).length()];
                            dataRAF.read(buffer);
                            String name = new String(buffer);
                            Double tavg = dataRAF.readDouble();
                            Double tmax = dataRAF.readDouble();
                            Log.v(TAG, "ID: " + name + " TAVG: " + tavg + " TMAX: " + tmax);
                        }
                        dataRAF.close();
                        adapter = new CityAdapter(getContext(), cityItemList);
                        listView.setAdapter(adapter);
                        mainFrame.setVisibility(View.VISIBLE);
                        api.closeDialog();
                    }
                }
            }
        } else {
            CityItem item;
            File f;
            if (date.equals(summerTime)) {
                Log.v(TAG, "Summer Data");
                f = new File(getActivity().getFilesDir(), summerValuesRAF);
            } else {
                Log.v(TAG, "Winter Data Data");
                f = new File(getActivity().getFilesDir(), winterValuesRAF);
            }
            dataRAF = new RandomAccessFile(f, "rw");
            for (int i = 0; i < cityNameList.size(); i++) {
                item = new CityItem(cityNameList.get(i), set_1_list.get(i), set_2_list.get(i));
                dataRAF.writeBytes(cityIDList.get(i));
                if (set_1_list.get(i) != null) {
                    dataRAF.writeDouble(set_1_list.get(i));
                } else {
                    dataRAF.writeDouble(-999);
                }
                if (set_2_list.get(i) != null) {
                    dataRAF.writeDouble(set_2_list.get(i));
                } else {
                    dataRAF.writeDouble(-999);
                }
                cityItemList.add(item);
            }
            dataRAF.seek(0);
            for (int j = 0; j < cityNameList.size(); j++) {
                byte[] buffer = new byte[cityIDList.get(j).length()];
                dataRAF.read(buffer);
                String name = new String(buffer);
                Double tavg = dataRAF.readDouble();
                Double tmax = dataRAF.readDouble();
                Log.v(TAG, "ID: " + name + " TAVG: " + tavg + " TMAX: " + tmax);
            }
            dataRAF.close();
            adapter = new CityAdapter(getContext(), cityItemList);
            listView.setAdapter(adapter);
            mainFrame.setVisibility(View.VISIBLE);
            api.closeDialog();
        }
    }

    void loadCacheData(CityJson model) {
        for (int i = 0; i < model.getData().size(); i++) {
            DataModel data = model.getData().get(i).getValue();
            String url = model.getData().get(i).getKey();
            if (url.contains(SET_1)) {
                dataSet = SET_1;
            } else {
                dataSet = SET_2;
            }
            postDataEvent(data, i, dataSet, url);
        }
        EventBus.getDefault().post(new FinishEvent(true, dataUUID, dataSet, true));
    }

    void postLocationEvent(LocationModel model, boolean exists) throws IOException {
        if (!exists) {
            ((MainActivity) getActivity()).writeAllLocations(model);
        } else {
            model = ((MainActivity) getActivity()).getLocationModel();
        }
        File f = new File(getActivity().getFilesDir(), btreeFile);
        btreeRAF = new RandomAccessFile(f, "rw");
        int a = model.getMetadata().getResultset().getLimit();
        int b = model.getMetadata().getResultset().getCount();
        int count = (a <= b) ? a : b;
        for (int c = 0; c < count; c++) {
            if (!cityNameList.contains(model.getResults().get(c).getName()) && !cityIDList.contains(model.getResults().get(c).getId())) {
                cityNameList.add(model.getResults().get(c).getName());
                cityIDList.add(model.getResults().get(c).getId());
                nameIDTable.insert(model.getResults().get(c).getName(), model.getResults().get(c).getId());
                try {
                    tree.insert( cityNameList.get(c));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        btreeRAF.seek(0);
        btreeRAF.close();
        api.closeDialog();
        api.showDialog("Loading TAVG", true, max);
        if (seasonModel.equals(summerModel)) {
            if (((MainActivity) getActivity()).getSummerModel() != null) {
                if (set_1_list != null) set_1_list = new ArrayList<>();
                if (set_2_list != null) set_2_list = new ArrayList<>();
                loadCacheData(((MainActivity) getActivity()).getSummerModel());
            } else {
                String[] params = new String[2];
                params[0] = date;
                params[1] = SET_1;
                new Task().execute(params);
            }
        } else if (seasonModel.equals(winterModel)) {
            if (((MainActivity) getActivity()).getWinterModel() != null) {
                if (set_1_list != null) set_1_list = new ArrayList<>();
                if (set_2_list != null) set_2_list = new ArrayList<>();
                loadCacheData(((MainActivity) getActivity()).getWinterModel());
            } else {
                String[] params = new String[2];
                params[0] = date;
                params[1] = SET_1;
                new Task().execute(params);
            }
        }
    }

    void postDataEvent(DataModel model, int index, String dataset, String url) {
        dataResults = model.getResults();
        Double tempTotal = null;
        Double pcpTotal = null;
        if (dataResults != null) {
            tempTotal = 0.0;
            pcpTotal = 0.0;
            int a = model.getMetadata().getResultset().getLimit();
            int b = model.getMetadata().getResultset().getCount();
            int count = (a <= b) ? a : b;
            int tempStations = 0;
            int pcpStations = 0;
            for (int c = 0; c < count; c++) {
                if (model.getResults().get(c).getDatatype().equals(SET_1)) {
                    tempTotal += model.getResults().get(c).getValue();
                    tempStations++;
                } else if (model.getResults().get(c).getDatatype().equals(SET_2)) {
                    pcpTotal += model.getResults().get(c).getValue();
                    pcpStations++;
                }
            }
            tempTotal = tempTotal / tempStations;
            pcpTotal = pcpTotal / pcpStations;
        }
        if (dataset.equals(SET_1)) {
            set_1_list.add(tempTotal);
        }
        if (dataset.equals(SET_2)) {
            set_2_list.add(pcpTotal);
        }
        callMade = true;
    }

    void setGlobalIndex(int globalIndex) {
        this.globalIndex = globalIndex;
    }

    private class Task extends AsyncTask<String, Void, Void> {
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
            while (l < cityNameList.size()) {
                Log.v(TAG, "Background task " + l);
                String start = strings[0];
                dataSet = strings[1];
                dataUUID = api.getData("GHCND", dataSet, cityIDList.get(globalIndex), start, start);
                while (!callMade) {
                    try {
                        //Need to have fun otherwise I'll go crazy
                        if (((MainActivity) getActivity()).isDevMode())
                            Log.v(TAG, "Sherlock is sleeping...");
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
                if (l < cityNameList.size()) {
                    globalIndex++;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        api.getDialog().setMessage(cityNameList.get(globalIndex));
                        api.getDialog().setProgress(globalIndex);
                    }
                });
            }

            finished = true;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new FinishEvent(finished, dataUUID, dataSet, false));
                }
            });

            return null;
        }
    }
}