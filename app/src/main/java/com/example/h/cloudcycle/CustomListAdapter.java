package com.example.h.cloudcycle;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by H on 31/01/2018.
 */

public class CustomListAdapter extends ArrayAdapter {
    private Activity context = null;

    private String[] priceArray = null;

    private String[] bikeIDArray = null;

    private String[] dateArray = null;

    public CustomListAdapter(Activity context, String[] priceArray, String[] bikeIDArray, String[] dateArray) {
        super(context, R.layout.listview_row, priceArray);

        this.context = context;
        this.priceArray = priceArray;
        this.bikeIDArray = bikeIDArray;
        this.dateArray = dateArray;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_row, null, true);

        //this code gets references to objects in the listview_row.xml file
        TextView priceTextField = (TextView) rowView.findViewById(R.id.H_price);
        TextView dateTextField = (TextView) rowView.findViewById(R.id.H_date);
        TextView bikeIDTextField = (TextView) rowView.findViewById(R.id.H_BikeID);

        //this code sets the values of the objects to values from the arrays
        priceTextField.setText("Price: " + priceArray[position]);
        dateTextField.setText("Date: " + dateArray[position]);
        bikeIDTextField.setText("BikeID: " + bikeIDArray[position]);

        return rowView;
    }

    ;


}
