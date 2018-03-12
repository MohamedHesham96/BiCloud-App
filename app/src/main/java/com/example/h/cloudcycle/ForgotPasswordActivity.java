package com.example.h.cloudcycle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.h.cloudcycle.WebServiceControl.ApiInterface;
import com.example.h.cloudcycle.WebServiceControl.ApiPassword;
import com.example.h.cloudcycle.WebServiceControl.ForgotPasswordResponse;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.email_ET)
    EditText email_ET;

    ForgotPasswordResponse fpResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passwrod);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    public void submitEmail(View view) {


        ApiInterface apiInterface = ApiPassword.getApiClient().create(ApiInterface.class);

        Call<ForgotPasswordResponse> call = apiInterface.forgetPassword("mrmedooo71@gmail.com");

        call.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {

                if (response.isSuccessful()) {

                    fpResponse = response.body();
                    Toast.makeText(ForgotPasswordActivity.this, fpResponse.getCode(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), EnterCode.class);
                    intent.putExtra("code", fpResponse.getCode());
                    startActivity(intent);
                } else {

                    Toast.makeText(ForgotPasswordActivity.this, "sdsadsad", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {

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
