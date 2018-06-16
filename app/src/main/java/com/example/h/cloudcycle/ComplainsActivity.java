package com.example.h.cloudcycle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.example.h.cloudcycle.WebServiceControl.ApiClient;
import com.example.h.cloudcycle.WebServiceControl.ApiInterface;
import com.example.h.cloudcycle.WebServiceControl.Complain;

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


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Complain>> call = apiInterface.getAllComplains("mobileApp", "bicloud_App2018#@");

        call.enqueue(new Callback<List<Complain>>() {
            @Override
            public void onResponse(Call call, Response response) {

                Toast.makeText(ComplainsActivity.this, "asdasd", Toast.LENGTH_SHORT).show();
                listComplains = (List<Complain>) response.body();

                if (!listComplains.isEmpty()) {

                    Toast.makeText(ComplainsActivity.this, listComplains.get(0).getContent(), Toast.LENGTH_SHORT).show();
                    ListView complainslistView = findViewById(R.id.complians_list_LV);

                    ComplainsListAdapter complainsListAdapter = new ComplainsListAdapter(getApplicationContext(), listComplains);

                    complainslistView.setAdapter(complainsListAdapter);

                } else {

                    Toast.makeText(ComplainsActivity.this, "Hello !!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(ComplainsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
}

