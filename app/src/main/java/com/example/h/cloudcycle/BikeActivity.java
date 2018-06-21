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
        TextView userDetail_tv = findViewById(R.id.user_detail);

        String bikeDetails = getIntent().getStringExtra("bikeDetail");
        String userId = null;

        if (userType.equals("user")) {

            userDetail_tv.setVisibility(View.INVISIBLE);

        } else {

            int start = bikeDetails.lastIndexOf(":") + 2;
            userId = bikeDetails.substring(start, bikeDetails.length());

            int start2 = bikeDetails.indexOf(" ");
            Toast.makeText(this, String.valueOf(start2), Toast.LENGTH_SHORT).show();
            bikeId = bikeDetails.substring(start2, bikeDetails.indexOf("N"));

        }

        bikeDetail_tv.setText(bikeDetails);

        userDetail_tv.setText(userId);

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
