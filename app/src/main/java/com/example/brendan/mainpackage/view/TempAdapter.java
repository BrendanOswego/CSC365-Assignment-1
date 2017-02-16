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
 * Adapter class for MainFragment ListView
 */

public class TempAdapter extends ArrayAdapter<TempItem> {


    public TempAdapter(Context context, ArrayList<TempItem> tempItemList){
        super(context,0,tempItemList);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TempItem item = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_temp_child, parent, false);
        }
        TextView name = (TextView)convertView.findViewById(R.id.view_temp_name);
        TextView fips = (TextView)convertView.findViewById(R.id.view_temp_fips);
        TextView value = (TextView)convertView.findViewById(R.id.view_temp_value);

        DecimalFormat df = new DecimalFormat("###.##");

        name.setText(item.getName());
        fips.setText(item.getFips());
        if(item.getValue() != null) {
            value.setText(df.format(item.getValue()));
        }else {
            value.setText(getContext().getResources().getString(R.string.no_info));
        }
        return convertView;
    }
}
