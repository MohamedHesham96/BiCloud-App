package com.example.h.cloudcycle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.h.cloudcycle.WebServiceControl.ApiClient;
import com.example.h.cloudcycle.WebServiceControl.ApiInterface;
import com.example.h.cloudcycle.WebServiceControl.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main2Activity extends AppCompatActivity {

    private ApiInterface apiInterface;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        String email = "moussa@yahoo.com";
        String password = "moussa123";

        Call<User> call = apiInterface.getUserInfo(email, password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                user = response.body();

                if (response.isSuccessful()) {
                    Log.d("Try To Login: ", user.toString());
                } else {
                    Log.d("Try To Login: ", "Not Working Nowwwwwwwwwww");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Log.d("Try To Login: ", "Failure");
            }
        });
    }
}
