package com.example.h.cloudcycle;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h.cloudcycle.WebServiceControl.ApiClient;
import com.example.h.cloudcycle.WebServiceControl.ApiInterface;
import com.example.h.cloudcycle.WebServiceControl.History;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {

    ArrayList<String> priceList;
    ArrayList<String> dateList;
    ArrayList<String> bikeIdList;
    ArrayList<String> distanceList;

    private ApiInterface apiInterface;
    private List<History> historysList = null;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSharedPreferences();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call call = apiInterface.getUserHistory(id);

        call.enqueue(new Callback<List<History>>() {
            @Override
            public void onResponse(Call call, Response response) {

                historysList = (List<History>) response.body();
                Log.d("Respons: ", "\nDeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeate: " + historysList.get(0).getDate()
                        + "Price: " + historysList.get(0).getPrice()
                        + "Distance: " + historysList.get(0).getDistance()
                        + "BikeID: " + historysList.get(0).getBike_id());
                ListView listView = (ListView) findViewById(R.id.historyList);
                listView.setAdapter(new CustomListAdapter(getApplicationContext(), historysList));
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

    }

    public void getSharedPreferences() {


        SharedPreferences sp = this.getSharedPreferences("Login", MODE_PRIVATE);

        // still there strings here... like (id.. balance....)

        if (sp != null) {

            String idSP = sp.getString("id", null);

            if (sp.contains("id")) {
                if (!idSP.equals("")) {

                    id = Integer.parseInt(idSP);

                    Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {

            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

