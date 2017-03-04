package com.example.brendan.mainpackage.onboarding;

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

import com.example.brendan.mainpackage.BaseFragment;
import com.example.brendan.mainpackage.CustomHashTable;
import com.example.brendan.mainpackage.EastCoastList;
import com.example.brendan.mainpackage.MainActivity;
import com.example.brendan.mainpackage.R;
import com.example.brendan.mainpackage.api.APIClass;
import com.example.brendan.mainpackage.event.DataEvent;
import com.example.brendan.mainpackage.event.FinishEvent;
import com.example.brendan.mainpackage.event.LocationEvent;
import com.example.brendan.mainpackage.model.DataResults;
import com.example.brendan.mainpackage.model.Metadata;
import com.example.brendan.mainpackage.model.Result;
import com.example.brendan.mainpackage.view.TempAdapter;
import com.example.brendan.mainpackage.view.TempItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
            mainFrame.setVisibility(View.GONE);
            startTime = ((MainActivity) getActivity()).getStartTime();
            endTime = startTime;
            fipsList = new ArrayList<>();
            locationTable = new CustomHashTable<>();
            table = new CustomHashTable<>();
            api = APIClass.getInstance();
            api.init(getActivity());
            File dir = getActivity().getFilesDir();
            String name = String.format(getString(R.string.location_write_format), startTime);
            File f = new File(dir, name);
            if (f.exists()) {
                Log.v(TAG, "File already exists");
            } else {
                locationUUID = api.getAllStates();
            }
            listView.setOnItemClickListener(listViewClickListener);
            listItems = new ArrayList<>();

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
    public void onLocationEvent(LocationEvent event){
        String name = String.format(getString(R.string.location_write_format), startTime);
        ((MainActivity)getActivity()).writeLocationInternal(event,name);
        if (startTime != null) {
            if (event.getUuid().equals(this.locationUUID)) {
                stateList = new ArrayList<>();
                List<Result> locationResults = event.getLocation().getResults();
                Metadata md_location = event.getLocation().getMetadata();
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
                api.showDialog("Searching for Temperatures", true);
                FIPSTask task = new FIPSTask();
                String[] dates = new String[2];
                dates[0] = startTime;
                dates[1] = endTime;
                task.execute(dates);
            }
        }

    }

    /**
     * Listens for DataEvent post from the APIClass.
     *
     * @param event DataEvent post created after APIClass CallBack from Web Service.
     */
    @Subscribe
    public void onDataEvent(DataEvent event) {
        if (event.getUuid().equals(this.dataResultsUUID)) {
            dataResults = event.getDataModel().getResults();
            Metadata md = event.getDataModel().getMetadata();
            if (dataResults != null) {
                int i = md.getResultset().getCount();
                int j = md.getResultset().getLimit();
                maxData = (i <= j) ? i : j;
            }
            postDataEvent(globalIndex);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            callMade = true;
        }
        dataResultsUUID = null;
    }

    /**
     * Listens for FinishEvent post from the FIPSTask.
     *
     * @param event FinishEvent post created after FIPSTask has returned null in doInBackground().
     */
    @Subscribe
    public void onFinishedEvent(FinishEvent event) {
        if (event.isFinished()) {
            adapter = new TempAdapter(getContext(), listItems);
            listView.setAdapter(adapter);
            String text = String.format(getResources().getString(R.string.date_picked), startTime);
            datePicked.setText(text);
            mainFrame.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Makes All API calls for dataResults of day specified in StartFragment
     *
     * @param index Index in the fipsList ArrayList for making API call.
     */
    private void postLocationEvent(int index) {
        globalIndex = index;
    }

    /**
     * Adds information to table CustomHashTable
     *
     * @param index index from fipsList ArrayList
     */
    private void postDataEvent(int index) {
        double total = 0;
        int tempStations = 0;
        TempItem temp;
        if (dataResults != null) {
            for (int i = 0; i < maxData; i++) {
                if (dataResults.get(i).getDatatype().equals("TAVG")) {
                    total += dataResults.get(i).getValue();
                    ++tempStations;
                }
            }
            total = total / tempStations;
            System.out.println("Total for index " + index + "==" + total);
            table.insert(fipsList.get(index), total);
            temp = new TempItem(stateList.get(index), fipsList.get(index), total);
        } else {
            temp = new TempItem(stateList.get(index), fipsList.get(index), null);
        }
        listItems.add(temp);
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
                    postLocationEvent(l);
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
