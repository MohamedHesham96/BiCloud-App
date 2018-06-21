package com.example.h.cloudcycle;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.h.cloudcycle.WebServiceControl.ApiClient;
import com.example.h.cloudcycle.WebServiceControl.ApiInterface;
import com.example.h.cloudcycle.WebServiceControl.GeneralResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {

    @BindView(R.id.content)
    EditText content_Ed;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        setTitle("Feedback");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ButterKnife.bind(this);

    }

    public void sendFeedback(View view) {

        sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);

        String content = content_Ed.getText().toString().trim();

        String idSP = sharedPreferences.getString("id", null);

        String userType = sharedPreferences.getString("type", null);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<GeneralResponse> call = null;

        if (userType.equals("user")) {

            call = apiInterface.createFeedbackForUser(idSP, content, "mobileApp", "bicloud_App2018#@");

        } else {

            call = apiInterface.createFeedbackForMaintenance(idSP, content, "mobileApp", "bicloud_App2018#@");

        }

        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                GeneralResponse generalResponse = response.body();

                if (response.isSuccessful()) {

                    if (generalResponse.isSuccess()) {

                        finish();
                        Toast.makeText(getApplicationContext(), "Thanks", Toast.LENGTH_LONG).show();

                    }

                } else {

                    Toast.makeText(getApplicationContext(), "Error !!", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error !!", Toast.LENGTH_LONG).show();

            }
        });
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
