package com.example.h.cloudcycle;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        ButterKnife.bind(this);


    }

    public void sendFeedback(View view) {

        Toast.makeText(this, "sssssssssssssss", Toast.LENGTH_SHORT).show();
        sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);

        String content = content_Ed.getText().toString().trim();

        String idSP = sharedPreferences.getString("id", null);
        String emailSP = sharedPreferences.getString("email", null);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<GeneralResponse> call = apiInterface.CreateFeedback(idSP, content, "mobileApp", "bicloud_App2018#@");

        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                GeneralResponse generalResponse = response.body();

                if (response.isSuccessful()) {

                    if (generalResponse.isSuccess()) {


                        Toast.makeText(getApplicationContext(), "Thanks", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } else {

                    Toast.makeText(getApplicationContext(), "Error !!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

}
