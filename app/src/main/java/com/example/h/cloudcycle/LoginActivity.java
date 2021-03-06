package com.example.h.cloudcycle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h.cloudcycle.Utility.HelperClass;
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
    @BindView(R.id.input_email)
    EditText _emailText;

    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.forget_password)
    TextView _forgotPassword;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    String email;
    String password;
    private ApiInterface apiInterface;
    private User user;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        checkSharedPreferences();

        ConstraintLayout linearLayout = findViewById(R.id.layout);

        linearLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                hideKeyboard(view);
                return false;
            }
        });

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

        _forgotPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

    }

    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void checkSharedPreferences() {

        SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);

        String emailSharedPreferences = sp1.getString("email", null);
        String passwordSharedPreferences = sp1.getString("password", null);

        Log.d("User Info: ", emailSharedPreferences + " | " + passwordSharedPreferences);

        if (sp1 != null) {

            if (emailSharedPreferences != null && passwordSharedPreferences != null) {

                onLoginSuccess();
            }
        }
    }

    public void login() {


        if (!HelperClass.isNetworkAvailable(this)) {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
            HelperClass.askPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, 2);
            return;
        } else if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        email = _emailText.getText().toString().trim();
        password = _passwordText.getText().toString().trim();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> call = apiInterface.getUserInfo(email, password, "mobileApp", "bicloud_App2018#@");

        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                user = response.body();

                if (user.isSuccess()) {

                    if (user.getVerified() == 1) {

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {

                                        onLoginSuccess();

                                        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                                        SharedPreferences.Editor Ed = sp.edit();

                                        Ed.putString("id", String.valueOf(user.getId()));
                                        Ed.putString("name", user.getName());
                                        Ed.putString("email", email);
                                        Ed.putString("password", password);
                                        Ed.putString("image", user.getImage());
                                        Ed.putString("type", user.getType());
                                        Toast.makeText(LoginActivity.this, "Type: " + user.getType(), Toast.LENGTH_SHORT).show();
                                        Ed.putString("balance", user.getBalance());

                                        Ed.commit();

                                        progressDialog.dismiss();
                                    }
                                }, 0);
                    } else {
                        _loginButton.setEnabled(true);
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, String.valueOf(user.getVerified()), Toast.LENGTH_SHORT).show();
                        Toast.makeText(LoginActivity.this, "Please Verify your Email..", Toast.LENGTH_LONG).show();
                    }
                } else {

                    progressDialog.dismiss();
                    _emailText.setError("Email or Password is incorrect !");
                    onLoginFailed();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                progressDialog.dismiss();
                _loginButton.setEnabled(true);
                Toast.makeText(LoginActivity.this, "Check Your Connection !", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }


    public void onLoginSuccess() {

        _loginButton.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), EdgeActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
        finish();

    }

    public void onLoginFailed() {

        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {

        boolean valid = true;

        String email = _emailText.getText().toString().trim();
        String password = _passwordText.getText().toString().trim();

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

