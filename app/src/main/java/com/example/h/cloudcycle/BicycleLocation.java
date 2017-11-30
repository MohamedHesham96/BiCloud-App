package com.example.h.cloudcycle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class BicycleLocation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bicycle_location);

        TextView code_tv = (TextView) findViewById(R.id.bicycle_code);
        code_tv.setText(getIntent().getStringExtra("code"));

        TextView location_tv = (TextView) findViewById(R.id.bicycle_location);
        location_tv.setText(getIntent().getStringExtra("location"));

    }
}
