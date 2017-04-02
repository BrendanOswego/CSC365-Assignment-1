package com.example.brendan.mainpackage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by brendan on 3/29/17.
 */

public class SelectionFragment extends BaseFragment {
    private static final String TAG = SelectionFragment.class.getName();

    @BindView(R.id.btn_assign_1)
    Button assign_1;

    @BindView(R.id.btn_assign_2)
    Button assign_2;

    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_selection, container, false);
        unbinder = ButterKnife.bind(this,view);
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

    @OnClick(R.id.btn_assign_1)
    public void A1Click() {
        Log.v(TAG,"Navigating to MainFragment");
        ((MainActivity) getActivity()).navigateToMain();
    }

    @OnClick(R.id.btn_assign_2)
    public void A2Click() {
        Log.v(TAG,"Navigating to CityFragment");
        ((MainActivity) getActivity()).navigateToCity();
    }


}
