package com.example.h.cloudcycle;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.h.cloudcycle.WebServiceControl.History;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H on 31/01/2018.
 */

public class CustomListAdapter extends ArrayAdapter<History> {

    private Context context = null;

    private List<History> historyList = null;

    public CustomListAdapter(Context context, List<History> historyList) {
        super(context, R.layout.listview_row, historyList);

        this.context = context;
        this.historyList = historyList;
    }

    public View getView(int position, View view, ViewGroup parent) {


        LayoutInflater inflater = LayoutInflater.from(context);

        View rowView = inflater.inflate(R.layout.listview_row, parent, false);

        //this code gets references to objects in the listview_row.xml file
        TextView priceTextField = (TextView) rowView.findViewById(R.id.H_price);
        TextView dateTextField = (TextView) rowView.findViewById(R.id.H_date);
        TextView bikeIDTextField = (TextView) rowView.findViewById(R.id.H_BikeID);

        //this code sets the values of the objects to values from the arraysy
        priceTextField.setText("Price: " + historyList.get(position).getPrice());
        dateTextField.setText("Date: " + historyList.get(position).getDate());
        bikeIDTextField.setText("BikeID: " + historyList.get(position).getBike_id());

        return rowView;
    }

}
