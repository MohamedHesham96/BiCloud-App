package com.example.h.cloudcycle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h.cloudcycle.WebServiceControl.ApiClient;
import com.example.h.cloudcycle.WebServiceControl.ApiInterface;
import com.example.h.cloudcycle.WebServiceControl.User;

import butterknife.BindView;
import butterknife.ButterKnife;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BikeActivity extends AppCompatActivity {

    private String bikeId;


    @BindView(R.id.user_info_layout)
    LinearLayout userInfoLayout;

    @BindView(R.id.bicycle_detail)
    TextView bikeDetail_tv;

    @BindView(R.id.bicycle_detail_layout)
    LinearLayout bikeDetailLayout_tv;

    @BindView(R.id.user_email_tv)
    TextView userEmail_tv;

    @BindView(R.id.user_name_tv)
    TextView userName_tv;


    @BindView(R.id.unlock_bike_BT)
    Button unlockBikeBT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bicycle_location);

        ButterKnife.bind(this);

        setTitle("Bike");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences sp = this.getSharedPreferences("Login", Context.MODE_PRIVATE);

        final String userType = sp.getString("type", null);

        final String bikeDetails = getIntent().getStringExtra("bikeDetail");

        String userId = null;

        if (userType.equals("user")) {

            userInfoLayout.setVisibility(View.INVISIBLE);


        } else {

            int start = bikeDetails.lastIndexOf(":") + 2;
            userId = bikeDetails.substring(start, bikeDetails.length());

        }

        int start2 = bikeDetails.indexOf(" ");
        bikeId = bikeDetails.substring(start2, bikeDetails.indexOf("N"));

        Toast.makeText(this, "bikeID: " + bikeId, Toast.LENGTH_SHORT).show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<User> call = apiInterface.getUserData(userId, "mobileApp", "bicloud_App2018#@");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                User userData = response.body();

                if (response.isSuccessful()) {

                    bikeDetail_tv.setText(bikeDetails);

                    userName_tv.setText(userData.getName());
                    userEmail_tv.setText(userData.getEmail());

                } else {

                    Toast.makeText(getApplicationContext(), "Error !!", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

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
