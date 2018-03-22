package com.example.h.cloudcycle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

public class UpdatePassword extends AppCompatActivity {


    @BindView(R.id.submit_new_password)
    Button submitNewPassword;


    EditText oldPasswordText;

    @BindView(R.id.password)
    EditText passwordText;

    @BindView(R.id.repeat_password)
    EditText repeatedPasswordText;

    SharedPreferences sp;
    String email;
    private GeneralResponse fpResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        ButterKnife.bind(this);

        submitNewPassword = findViewById(R.id.submit_new_password);
        passwordText = findViewById(R.id.password);
        repeatedPasswordText = findViewById(R.id.repeat_password);

        email = sp.getString("email", null);
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void submitPassword(View view) {

        if (!validate()) {

            onSignupFailed();
            return;
        }

        onSignupSuccess();
    }

    public void onSignupSuccess() {
        submitNewPassword.setEnabled(true);
        sendPassword();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign-Up Failed", Toast.LENGTH_LONG).show();

        submitNewPassword.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String password = passwordText.getText().toString().trim();
        String repeatdPassword = repeatedPasswordText.getText().toString().trim();


        if (password.isEmpty() || passwordText.length() < 6 || passwordText.length() > 32) {

            passwordText.setError("between 6 and 32 alphanumeric characters");
            valid = false;

        } else if (!password.equals(repeatdPassword)) {

            passwordText.setError("Password Dose Not Match");
            repeatedPasswordText.setError("Password Dose Not Match");
            valid = false;

        } else {

            passwordText.setError(null);

        }

        return valid;
    }

    public void sendPassword() {


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
        Call<GeneralResponse> call = apiInterface.resetPassword(email, passwordText.getText().toString().trim());

        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {


                if (response.isSuccessful()) {

                    Toast.makeText(UpdatePassword.this, String.valueOf(response.body().isSuccess()), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    finish();
                    startActivity(intent);

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
