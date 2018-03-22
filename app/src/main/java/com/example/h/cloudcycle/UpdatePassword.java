package com.example.h.cloudcycle;

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

    @BindView(R.id.oldPassword)
    EditText oldPassword_ET;

    @BindView(R.id.password)
    EditText password_ET;

    @BindView(R.id.repeat_password)
    EditText repeatedPassword_ET;

    SharedPreferences sp;
    String emailSP;
    String idSP;
    String oldPassword;
    String newPassword;
    String repeatedPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        ButterKnife.bind(this);

        sp = getSharedPreferences("Login", MODE_PRIVATE);

        idSP = sp.getString("id", null);
        emailSP = sp.getString("email", null);

        Toast.makeText(this, "Email: " + emailSP, Toast.LENGTH_SHORT).show();
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

        onSubmitSuccess();
    }

    public void onSubmitSuccess() {

        submitNewPassword.setEnabled(true);

        oldPassword = oldPassword_ET.getText().toString().trim();
        newPassword = password_ET.getText().toString().trim();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Toast.makeText(this, emailSP, Toast.LENGTH_SHORT).show();
        Call<GeneralResponse> call = apiInterface.updateUserPassword(idSP, emailSP, oldPassword, newPassword);

        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                GeneralResponse generalResponse = response.body();

                if (response.isSuccessful()) {

                    if (generalResponse.isSuccess()) {

                        Toast.makeText(UpdatePassword.this, String.valueOf(generalResponse.isSuccess()), Toast.LENGTH_SHORT).show();
                        Toast.makeText(UpdatePassword.this, "Password updated Successfully", Toast.LENGTH_LONG).show();
                        finish();

                    } else {

                        oldPassword_ET.setError("Your Current Password is incorrect");
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

    public void onSignupFailed() {

        Toast.makeText(getBaseContext(), "Submit Failed", Toast.LENGTH_LONG).show();
        submitNewPassword.setEnabled(true);
    }

    public boolean validate() {

        boolean valid = true;

        String oldPassword = oldPassword_ET.getText().toString().trim();
        String password = password_ET.getText().toString().trim();
        String repeatdPassword = repeatedPassword_ET.getText().toString().trim();


        if (oldPassword.isEmpty() || oldPassword.length() < 6 || oldPassword.length() > 32) {

            oldPassword_ET.setError("Enter Your Current Password");
            valid = false;

        } else {

            oldPassword_ET.setError(null);

        }

        if (password.isEmpty() || password_ET.length() < 6 || password_ET.length() > 32) {

            password_ET.setError("between 6 and 32 alphanumeric characters");
            valid = false;

        } else if (!password.equals(repeatdPassword)) {

            password_ET.setError("Password Dose Not Match");
            repeatedPassword_ET.setError("Password Dose Not Match");
            valid = false;

        } else {

            password_ET.setError(null);
        }

        return valid;
    }

}
