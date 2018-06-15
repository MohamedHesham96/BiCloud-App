package com.example.h.cloudcycle;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h.cloudcycle.WebServiceControl.ApiClient;
import com.example.h.cloudcycle.WebServiceControl.ApiInterface;
import com.example.h.cloudcycle.WebServiceControl.GeneralResponse;
import com.example.h.cloudcycle.WebServiceControl.History;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public void clearData() {

        historyList.clear();
    }

    public View getView(final int position, View view, final ViewGroup parent) {


        LayoutInflater inflater = LayoutInflater.from(context);

        View rowView = inflater.inflate(R.layout.listview_row, parent, false);

        //this code gets references to objects in the listview_row.xml file
        TextView priceTextField = rowView.findViewById(R.id.H_price);
        TextView dateTextField = rowView.findViewById(R.id.H_date);
        TextView bikeIDTextField = rowView.findViewById(R.id.H_BikeID);

        //this code sets the values of the objects to values from the arraysy
        priceTextField.setText("Price: " + historyList.get(position).getPrice());
        dateTextField.setText("Date: " + historyList.get(position).getDate());
        bikeIDTextField.setText("BikeID: " + historyList.get(position).getBike_id());

        ImageView bt = rowView.findViewById(R.id.delete_history_BT);
        //important so we know which item to delete on button click

        SharedPreferences sp = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String idSP = sp.getString("id", null);
        final String historyId = historyList.get(position).getId();
        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call call = apiInterface.deleteOneHistory(historyId, idSP, "mobileApp", "bicloud_App2018#@");

                call.enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call call, Response response) {

                        GeneralResponse generalResponse = (GeneralResponse) response.body();

                        Toast.makeText(parent.getContext(), "Flag: " + String.valueOf(generalResponse.isSuccess()), Toast.LENGTH_SHORT).show();

                        if (generalResponse.isSuccess()) {
                            removeItem(position);
                            notifyDataSetChanged();
                            Toast.makeText(parent.getContext(), "Delete is Done", Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(parent.getContext(), "Delete is Not Done", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Toast.makeText(parent.getContext(), "Error !!", Toast.LENGTH_SHORT).show();

                    }
                });
            }

        });

        return rowView;

    }

    public void removeItem(int position) {

        historyList.remove(position);

        notifyDataSetChanged();

    }


}
