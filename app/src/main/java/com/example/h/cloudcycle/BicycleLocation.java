package com.example.h.cloudcycle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class BicycleLocation extends AppCompatActivity {

    private String bikeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bicycle_location);

        SharedPreferences sp = this.getSharedPreferences("Login", Context.MODE_PRIVATE);

        final String userType = sp.getString("type", null);

        TextView bikeDetail_tv = findViewById(R.id.bicycle_detail);
        TextView userDetail_tv = findViewById(R.id.user_detail);

        String bikeDetails = getIntent().getStringExtra("bikeDetail");
        String userId = null;

        if (userType.equals("user")) {

            userDetail_tv.setVisibility(View.INVISIBLE);

        } else {

            int start = bikeDetails.lastIndexOf(" ");
            userId = bikeDetails.substring(start, bikeDetails.length()).trim();

            int start2 = bikeDetails.indexOf(" ");
            Toast.makeText(this, String.valueOf(start2), Toast.LENGTH_SHORT).show();
            bikeId = bikeDetails.substring(start2, bikeDetails.indexOf("N")).trim();

        }

        bikeDetail_tv.setText(bikeDetails);

        userDetail_tv.setText(userId);

    }

    public void openComplainsMenu(View view) {

        Intent intent = new Intent(this, ComplainsActivity.class);

        intent.putExtra("bikeId", bikeId);
        startActivity(intent);
    }
}
