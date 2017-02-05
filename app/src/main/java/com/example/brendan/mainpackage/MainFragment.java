package com.example.brendan.mainpackage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.brendan.mainpackage.api.APIClass;
import com.example.brendan.mainpackage.event.DataEvent;
import com.example.brendan.mainpackage.event.LocationEvent;
import com.example.brendan.mainpackage.model.DataResults;
import com.example.brendan.mainpackage.model.Metadata;
import com.example.brendan.mainpackage.model.Result;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Fragment class for making API calls, using EventBus to listen for APIClass Event posts
 */

public class MainFragment extends BaseFragment {

    private static final String TAG = MainFragment.class.getName();

    private UUID locationUUID;
    private UUID dataResultsUUID;
    EastCoastList ecList = new EastCoastList();
    CustomHashTable<String, Float> table;
    private List<DataResults> dataResults;

    private int maxData;
    private static final String start = "2016-01-01";
    private static final String end = "2016-01-31";
    private List<String> fipsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        table = new CustomHashTable<>();
        fipsList = new ArrayList<>();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_main, container, false);
        locationUUID = APIClass.getInstance().getAllStates();
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
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onLocationEvent(LocationEvent event) {
        if (event.getUuid().equals(this.locationUUID)) {
            List<Result> locationResults = event.getLocation().getResults();
            Metadata md_location = event.getLocation().getMetadata();
            int i = md_location.getResultset().getCount();
            int j = md_location.getResultset().getLimit();
            int count = (i <= j) ? i : j;
            for (int k = 0; k < count; k++) {
                for (int l = 0; l < ecList.size(); l++) {
                    if (locationResults.get(k).getName().equalsIgnoreCase(ecList.getList().get(l))
                            && !fipsList.contains(locationResults.get(k).getId()))
                        fipsList.add(locationResults.get(k).getId());
                }

            }
            System.out.println("FIPS SIZE: " + fipsList.size());
            postLocationEvent(0);
        }
    }

    @Subscribe
    public void onDataEvent(DataEvent event) {
        if (event.getUuid().equals(this.dataResultsUUID)) {
            dataResults = event.getDataModel().getResults();
            Metadata md = event.getDataModel().getMetadata();
            int i = md.getResultset().getCount();
            int j = md.getResultset().getLimit();
            maxData = (i <= j) ? i : j;
            postDataEvent();
        }
    }

    private void postDataEvent() {
        for (int i = 0; i < maxData ; i++) {
            table.insert(dataResults.get(i).getDatatype(), dataResults.get(i).getValue());
        }
        for (int i = 0; i < maxData; i++) {
            System.out.println(table.search(dataResults.get(i).getDatatype()));
        }

    }

    private void postLocationEvent(int i) {
        dataResultsUUID = APIClass.getInstance().getData("GSOM", fipsList.get(i), start, end);
    }

    private void showFIPS() {
        for (int i = 0; i < fipsList.size(); i++) {
            System.out.println(fipsList.get(i));
        }
    }

}
