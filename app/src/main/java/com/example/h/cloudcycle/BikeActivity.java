package com.example.h.cloudcycle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h.cloudcycle.WebServiceControl.ApiClient;
import com.example.h.cloudcycle.WebServiceControl.ApiInterface;
import com.example.h.cloudcycle.WebServiceControl.GeneralResponse;
import com.example.h.cloudcycle.WebServiceControl.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BikeActivity extends AppCompatActivity {

    private String bikeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bicycle_location);

        setTitle("Bike");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences sp = this.getSharedPreferences("Login", Context.MODE_PRIVATE);

        final String userType = sp.getString("type", null);

        TextView bikeDetail_tv = findViewById(R.id.bicycle_detail);
        final TextView userDetail_tv = findViewById(R.id.user_detail);

        String bikeDetails = getIntent().getStringExtra("bikeDetail");
        String userId = null;

        if (userType.equals("user")) {

            userDetail_tv.setVisibility(View.INVISIBLE);

        } else {

            int start = bikeDetails.lastIndexOf(":") + 2;
            userId = bikeDetails.substring(start, bikeDetails.length());

        }

        int start2 = bikeDetails.indexOf(" ");
        Toast.makeText(this, String.valueOf(start2), Toast.LENGTH_SHORT).show();
        bikeId = bikeDetails.substring(start2, bikeDetails.indexOf("N"));

        //TO Do Work >> get user data to display
        bikeDetail_tv.setText(bikeDetails);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<User> call = apiInterface.getUserData(userId, "mobileApp", "bicloud_App2018#@");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                User userData = response.body();


                if (userData.isSuccess()) {

                    Toast.makeText(getApplicationContext(), "Done Successfully", Toast.LENGTH_LONG).show();
                    userDetail_tv.setText(userData.toString());

                } else {

                    Toast.makeText(getApplicationContext(), "Error !!", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Error !!", Toast.LENGTH_LONG).show();

            }
        });




    }

    public void openComplainsMenu(View view) {

        Intent intent = new Intent(this, ComplainsActivity.class);

        intent.putExtra("bikeId", bikeId);
        startActivity(intent);
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
