package com.example.h.cloudcycle;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.h.cloudcycle.WebServiceControl.ApiClient;
import com.example.h.cloudcycle.WebServiceControl.ApiInterface;
import com.example.h.cloudcycle.WebServiceControl.GeneralResponse;
import com.example.h.cloudcycle.WebServiceControl.History;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {


    private ApiInterface apiInterface;
    private List<History> historysList = null;
    private String id;
    ListView listView;
    CustomListAdapter customListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSharedPreferences();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call call = apiInterface.getUserHistory(id, "mobileApp", "bicloud_App2018#@");

        call.enqueue(new Callback<List<History>>() {
            @Override
            public void onResponse(Call call, Response response) {

                historysList = (List<History>) response.body();
                if (!historysList.isEmpty()) {
                    customListAdapter = new CustomListAdapter(getApplicationContext(), historysList);

                    listView = findViewById(R.id.historyList);

                    listView.setAdapter(customListAdapter);

                } else {

                    Toast.makeText(HistoryActivity.this, "No History", Toast.LENGTH_SHORT).show();
                }

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

                    id = idSP;

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


    public void deleteAllHistory(final View view) {


        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call call = apiInterface.deleteAllHistory(id, "mobileApp", "bicloud_App2018#@");

        call.enqueue(new Callback<GeneralResponse>() {

            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                GeneralResponse generalResponse = response.body();

                if (generalResponse.isSuccess()) {
                    customListAdapter.clearData();
                    listView.setAdapter(customListAdapter);
                    Toast.makeText(HistoryActivity.this, "Delete All History is Done", Toast.LENGTH_SHORT).show();
                    view.setVisibility(View.INVISIBLE);

                } else {

                    Toast.makeText(HistoryActivity.this, "Delete All History is not Done", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

    }
}