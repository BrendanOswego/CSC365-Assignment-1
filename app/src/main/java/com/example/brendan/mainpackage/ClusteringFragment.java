package com.example.brendan.mainpackage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.brendan.mainpackage.api.APIClass;
import com.example.brendan.mainpackage.datastrctures.BTree;
import com.example.brendan.mainpackage.datastrctures.CustomHashTable;
import com.example.brendan.mainpackage.datastrctures.KMeans;
import com.example.brendan.mainpackage.datastrctures.Node;
import com.example.brendan.mainpackage.event.CityDataEvent;
import com.example.brendan.mainpackage.event.CityLocationEvent;
import com.example.brendan.mainpackage.event.FinishEvent;
import com.example.brendan.mainpackage.model.DataModel;
import com.example.brendan.mainpackage.model.DataResults;
import com.example.brendan.mainpackage.model.LocationModel;
import com.example.brendan.mainpackage.model.NodeEntry;
import com.example.brendan.mainpackage.view.CityAdapter;
import com.example.brendan.mainpackage.view.CityItem;
import com.example.brendan.mainpackage.view.TempItem;
import com.google.gson.internal.bind.ArrayTypeAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.GONE;

/**
 * Created by brendan on 4/10/17.
 */
@SuppressWarnings("unchecked")
public class ClusteringFragment extends BaseFragment {
    private static final String TAG = ClusteringFragment.class.getName();

    @BindView(R.id.btn_nav_back_cluster)
    Button back;
    @BindView(R.id.listview_cluster)
    ListView listView;
    @BindView(R.id.spinner_cluster)
    Spinner spinner;

    ArrayList<Double> tavg;
    ArrayList<Double> tmax;
    ArrayList<String> cityNamesList;
    ArrayList<CityItem> cityItemList;
    ArrayList<NodeEntry> entries;
    BTree tree;
    Unbinder unbinder;

    KMeans clusterTavg;
    KMeans clusterTMax;
    CityAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle b = getArguments();
        tavg = (ArrayList<Double>) b.getSerializable("tavg");
        tmax = (ArrayList<Double>) b.getSerializable("tmax");
        entries = (ArrayList<NodeEntry>) b.getSerializable("values");
        tree = (BTree) b.getSerializable("btree");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_clustering, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (savedInstanceState == null) {
            clusterTavg = new KMeans(4, tavg);
            cityItemList = new ArrayList<>();
            cityNamesList = new ArrayList<>();
        }
        ArrayList<String> spinnerTitles = new ArrayList<>();
        for (int i = 0; i < clusterTavg.getGroups().size(); i++) {
            spinnerTitles.add(String.valueOf(i));
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerTitles);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (adapter != null) {
                    cityItemList = new ArrayList<>();
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                    cityNamesList = new ArrayList<>();
                }
                CityItem temp;
                for (int i = 0; i < clusterTavg.getGroups().get(position).size(); i++) {
                    Double value = clusterTavg.getGroups().get(position).get(i);
                    for (int j = 0; j < entries.size(); j++) {
                        if (value.equals(entries.get(j).getTavg()) && !cityNamesList.contains(entries.get(j).getName())) {
                            temp = new CityItem(entries.get(j).getName(), entries.get(j).getTavg(), entries.get(j).getTmax());
                            cityItemList.add(temp);
                            cityNamesList.add(entries.get(j).getName());
                        }
                    }
                }

                adapter = new CityAdapter(getContext(), cityItemList);
                listView.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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

    @OnClick(R.id.btn_nav_back_cluster)
    public void navigateToCity() {
        ((MainActivity) getActivity()).navigateToCity();
    }


}
