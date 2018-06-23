package com.example.h.cloudcycle;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h.cloudcycle.WebServiceControl.ApiClient;
import com.example.h.cloudcycle.WebServiceControl.ApiInterface;
import com.example.h.cloudcycle.WebServiceControl.Complain;
import com.example.h.cloudcycle.WebServiceControl.GeneralResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplainsActivity extends AppCompatActivity {


    List<Complain> listComplains = new ArrayList<Complain>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complains);
        setTitle("Choose your complain...");
        final ListView complainsListView = findViewById(R.id.complians_list_LV);

        final String bikeId = getIntent().getStringExtra("bikeId");

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Complain>> call = apiInterface.getAllComplains("mobileApp", "bicloud_App2018#@");

        call.enqueue(new Callback<List<Complain>>() {
            @Override
            public void onResponse(Call call, Response response) {

                listComplains = (List<Complain>) response.body();

                if (!listComplains.isEmpty()) {

                    ComplainsListAdapter complainsListAdapter = new ComplainsListAdapter(getApplicationContext(), listComplains);

                    complainsListView.setAdapter(complainsListAdapter);

                } else {

                    Toast.makeText(ComplainsActivity.this, "", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

                Toast.makeText(ComplainsActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

            }
        });


        complainsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView complianId_TV = view.findViewById(R.id.complain_id_TV);


                String complainId = complianId_TV.getText().toString();

                SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);

                String userId = sp.getString("id", null);
                String userType = sp.getString("type", null);


                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<GeneralResponse> call;

                if (userType.equals("user")) {

                    call = apiInterface.makeComplainForUser(bikeId, complainId, userId, "mobileApp", "bicloud_App2018#@");

                } else {

                    call = apiInterface.makeComplainForSuperVisor(bikeId, complainId, userId, "mobileApp", "bicloud_App2018#@");
                }

                call.enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call call, Response response) {

                        GeneralResponse generalResponse = (GeneralResponse) response.body();

                        if (generalResponse.isSuccess()) {

                            Toast.makeText(ComplainsActivity.this, "Complain is sent", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {

                            Toast.makeText(ComplainsActivity.this, "Error !!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Toast.makeText(ComplainsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });


    }
}

