package com.example.brendan.mainpackage.datastrctures;

import android.util.Log;

import com.example.brendan.mainpackage.model.NodeEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class KMeans {
    private static final String TAG = KMeans.class.getName();

    ArrayList<ArrayList<Double>> groups;

    public KMeans(int k, ArrayList<Double> list) {
        int numItems = list.size();
        ArrayList<Double> dataItems = new ArrayList<>();
        ArrayList<Double> cz = new ArrayList<>();
        ArrayList<Double> oldCz = new ArrayList<>();
        ArrayList<Double> row = new ArrayList<>();
        groups = new ArrayList<>();
        HashMap<String, Double> map = new HashMap<>();
        for (int i = 0; i < k; i++) {
            groups.add(new ArrayList<Double>());
        }
        for (int i = 0; i < numItems; i++) {
            if (list.get(i) == null) list.set(i, -999.0);
            dataItems.add(list.get(i));
            if (i < k) {
                cz.add(dataItems.get(i));
            }
        }
        int iter = 1;
        do {
            for (Double item : dataItems) {
                for (Double c : cz) {
                    if (item == null) item = -999.0;
                    row.add(Math.abs(c - item));
                }
                groups.get(row.indexOf(Collections.min(row))).add(item);
                row.removeAll(row);
            }
            for (int i = 0; i < k; i++) {
                if (iter == 1) {
                    oldCz.add(cz.get(i));
                } else {
                    oldCz.set(i, cz.get(i));
                }
                if (!groups.get(i).isEmpty()) {
                    cz.set(i, average(groups.get(i)));
                }
            }
            if (!cz.equals(oldCz)) {
                for (int i = 0; i < groups.size(); i++) {
                    groups.get(i).removeAll(groups.get(i));
                }
            }
            iter++;
        } while (!cz.equals(oldCz));

        for (int i = 0; i < groups.size(); i++) {
            Log.v(TAG, "New C " + (i + 1) + " " + cz.get(i));
        }
        for (int i = 0; i < groups.size(); i++) {
            Log.v(TAG, "Group " + (i + 1));
            Log.v(TAG, groups.get(i).toString());
        }
        System.out.println("Number of Itrations: " + iter);
    }

    private Double average(ArrayList<Double> list) {
        Double sum = 0.0;
        for (Double value : list) {
            sum = sum + value;
        }
        return sum / list.size();
    }

    public ArrayList<ArrayList<Double>> getGroups() {
        return groups;
    }

}