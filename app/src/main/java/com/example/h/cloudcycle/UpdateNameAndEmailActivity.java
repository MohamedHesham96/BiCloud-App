package com.example.h.cloudcycle;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
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

    @BindView(R.id.email_check)
    ImageView emailCheck_IV;

    ApiInterface apiInterface;
    String userId;

    String oldUserName;
    String oldUserEmail;

    String nameValue;
    String emailValue;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name_and_email);

        ButterKnife.bind(this);

        sp = getSharedPreferences("Login", MODE_PRIVATE);

        userName_ET.setText(getIntent().getStringExtra("userName"));
        userEmail_ET.setText(getIntent().getStringExtra("userEmail"));

        oldUserName = userName_ET.getText().toString();
        oldUserEmail = userEmail_ET.getText().toString();

    }

    public void submitChanges(View view) {


        userId = sp.getString("id", null);

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

        if (!oldUserName.equals(nameValue) && !oldUserEmail.equals(emailValue)) {

            oldUserName = userName_ET.getText().toString();
            oldUserEmail = userEmail_ET.getText().toString();

            final ProgressDialog progressDialog = new ProgressDialog(UpdateNameAndEmailActivity.this,
                    R.style.Theme_AppCompat_DayNight_Dialog);

            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {

                            nameUpdate(userId, nameValue);
                            emailUpdate(userId, emailValue);
                        }
                    }, 5000);

            progressDialog.dismiss();

            Toast.makeText(this, "name SP: " + sp.getString("name", null), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Email SP: " + sp.getString("email", null), Toast.LENGTH_SHORT).show();

            String email = sp.getString("email", null);
            String name = sp.getString("name", null);

            if (email.equals(oldUserEmail) && name.equals(oldUserName)) {

                this.finish();

            }


        } else if (!oldUserName.equals(nameValue) && isNameValid) {

            nameUpdate(userId, nameValue);
            oldUserName = userName_ET.getText().toString();
            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();

        } else if (!oldUserEmail.equals(emailValue) && isEmailValid) {

            emailUpdate(userId, emailValue);
            oldUserEmail = userEmail_ET.getText().toString();
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
                        SharedPreferences.Editor Ed = sp.edit();
                        Ed.putString("name", oldUserName);
                        Toast.makeText(UpdateNameAndEmailActivity.this, "Name Response: " + oldUserName, Toast.LENGTH_SHORT).show();
                        Ed.commit();

                    }

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
                        SharedPreferences.Editor Ed = sp.edit();
                        Ed.putString("email", oldUserEmail);
                        Toast.makeText(UpdateNameAndEmailActivity.this, "Email Response: " + oldUserEmail, Toast.LENGTH_SHORT).show();

                        Ed.commit();

                    } else {

                        if (!generalResponse.isSuccess()) {

                            userEmail_ET.setError("Duplicate  Email !");

                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

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
