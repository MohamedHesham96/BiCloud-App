package com.example.h.cloudcycle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h.cloudcycle.WebServiceControl.ApiClient;
import com.example.h.cloudcycle.WebServiceControl.ApiInterface;
import com.example.h.cloudcycle.WebServiceControl.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private ApiInterface apiInterface;
    private User user;

    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;

    String email;
    String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //    checkSharedPreferences();
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void checkSharedPreferences() {

        SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);

        String emailSharedPreferences = sp1.getString("email", null);
        String passwordSharedPreferences = sp1.getString("password", null);

        if (sp1 != null) {

            if (sp1.contains("email")) {
                if (!emailSharedPreferences.equals("") && !passwordSharedPreferences.equals("")) {

                    onLoginSuccess();
                }
            }
        }
    }

    public void login() {

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> call = apiInterface.getUserInfo(email, password);

        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                user = response.body();
                if (response.isSuccessful()) {

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {

                                    onLoginSuccess();

                                    SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                                    SharedPreferences.Editor Ed = sp.edit();
                                    Ed.putString("email", email);
                                    Ed.putString("password", password);
                                    Ed.putString("id", String.valueOf(user.getId()));
                                    Ed.putString("name", user.getName());
                                    Ed.putString("balance", user.getBalance());
                                    Ed.putString("image", user.getImage());
                                    Ed.commit();

                                    progressDialog.dismiss();
                                }
                            }, 1000);

                } else {
                    progressDialog.dismiss();
                    _emailText.setError("Error in Email or Password");
                    onLoginFailed();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void setSharedPreferences() {

        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("email", email);
        Ed.putString("password", password);
        Ed.putString("id", String.valueOf(user.getId()));
        Ed.putString("name", user.getName());
        Ed.commit();
    }
    public void onLoginSuccess() {

        _loginButton.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), EdgeActivity.class);
        finish();
        startActivityForResult(intent, REQUEST_SIGNUP);

    }

    public void onLoginFailed() {

        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {

        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 32) {
            _passwordText.setError("between 6 and 32 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
    }
}