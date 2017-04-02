package com.example.brendan.mainpackage.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.brendan.mainpackage.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by brendan on 3/29/17.
 */

public class CityAdapter extends ArrayAdapter<CityItem> {

    public CityAdapter(Context context, ArrayList<CityItem> tempItemList) {
        super(context, 0, tempItemList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CityItem item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.city_temp_child, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.view_city_name);
        TextView temp = (TextView) convertView.findViewById(R.id.view_city_temp);
        TextView precip = (TextView) convertView.findViewById(R.id.view_city_precip);
        DecimalFormat df = new DecimalFormat("###.##");
        name.setText(item.getName());
        if (item.getTemp() != null) {
            temp.setText(df.format(item.getTemp()));
        } else {
            temp.setText(getContext().getResources().getString(R.string.no_info));
        }
        if (item.getPrcp() != null) {
            precip.setText(df.format(item.getPrcp()));
        } else {
            precip.setText(getContext().getResources().getString(R.string.no_info));
        }
        return convertView;
    }
}
