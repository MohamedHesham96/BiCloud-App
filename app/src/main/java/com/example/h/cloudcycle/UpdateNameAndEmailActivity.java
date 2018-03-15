package com.example.h.cloudcycle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.h.cloudcycle.WebServiceControl.ApiClient;
import com.example.h.cloudcycle.WebServiceControl.ApiInterface;
import com.example.h.cloudcycle.WebServiceControl.GeneralResponse;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateNameAndEmailActivity extends AppCompatActivity {

    @BindView(R.id.user_name)
    EditText userName_ET;

    @BindView(R.id.user_email)
    EditText userEmail_ET;

    @BindView(R.id.name_check)
    ImageView nameCheck_IV;


    @BindView(R.id.done_submit)
    Button doneSubmit_BT;

    @BindView(R.id.email_check)
    ImageView emailCheck_IV;

    ApiInterface apiInterface;
    String userId;

    String oldUserName;
    String oldUserEmail;

    String nameValue;
    String emailValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name_and_email);

        ButterKnife.bind(this);

        userName_ET.setText(getIntent().getStringExtra("userName"));
        userEmail_ET.setText(getIntent().getStringExtra("userEmail"));

        oldUserName = userName_ET.getText().toString();
        oldUserEmail = userEmail_ET.getText().toString();

    }

    public void submitChanges(View view) {


        userId = this.getSharedPreferences("Login", MODE_PRIVATE).getString("id", null);

        Toast.makeText(this, userId, Toast.LENGTH_SHORT).show();
        nameValue = userName_ET.getText().toString();
        emailValue = userEmail_ET.getText().toString();


        boolean isNameValid = Pattern.matches("^[a-zA-Z_ ]*$", nameValue);
        boolean isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches();


        if (nameValue.isEmpty() || nameValue.length() < 3) {

            userName_ET.setError("at least 3 characters");

        } else if (!isNameValid) {

            userName_ET.setError("enter a valid email address");

        } else {

            userName_ET.setError(null);
        }

        if (emailValue.isEmpty() || !isEmailValid) {

            userEmail_ET.setError("enter a valid email address");
            return;

        } else {

            userEmail_ET.setError(null);
        }

        if (!oldUserName.equals(nameValue) && !oldUserEmail.equals(emailValue) && userEmail_ET.isEnabled() && userName_ET.isEnabled()) {

            nameUpdate(userId, nameValue);
            emailUpdate(userId, emailValue);


        } else if (!oldUserName.equals(nameValue) && userName_ET.isEnabled() && isNameValid) {

            nameUpdate(userId, nameValue);
            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();

        } else if (!oldUserEmail.equals(emailValue) && userEmail_ET.isEnabled() && isEmailValid) {

            emailUpdate(userId, emailValue);
            Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(this, "No Changes", Toast.LENGTH_SHORT).show();
        }

    }

    public void nameUpdate(String userId, String userName) {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<GeneralResponse> call = apiInterface.updateUserName(userId, userName);

        call.enqueue(new Callback<GeneralResponse>() {

            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                if (response.isSuccessful()) {

                    GeneralResponse generalResponse = response.body();

                    if (generalResponse.isSuccess()) {

                        nameCheck_IV.setImageResource(R.drawable.checked_icon);
                        userName_ET.setEnabled(false);
                        doneSubmit_BT.setVisibility(View.VISIBLE);
                    }

                    Toast.makeText(UpdateNameAndEmailActivity.this, "Name: " + String.valueOf(generalResponse.isSuccess()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });

    }

    public void emailUpdate(String userId, String userEmail) {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<GeneralResponse> call = apiInterface.updateUserEmail(userId, userEmail);

        call.enqueue(new Callback<GeneralResponse>() {

            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                if (response.isSuccessful()) {

                    GeneralResponse generalResponse = response.body();

                    if (generalResponse.isSuccess()) {

                        emailCheck_IV.setImageResource(R.drawable.checked_icon);
                        userEmail_ET.setEnabled(false);
                        doneSubmit_BT.setVisibility(View.VISIBLE);
                    }

                    Toast.makeText(UpdateNameAndEmailActivity.this, "email: " + String.valueOf(generalResponse.isSuccess()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });

    }


    public void doneClick(View view) {

        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("name", nameValue);
        Ed.putString("email", emailValue);
        Ed.commit();


        this.finish();
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
