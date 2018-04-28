package com.example.h.cloudcycle;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h.cloudcycle.Utility.HelperClass;
import com.example.h.cloudcycle.WebServiceControl.ApiClient;
import com.example.h.cloudcycle.WebServiceControl.ApiInterface;
import com.example.h.cloudcycle.WebServiceControl.RealPathUtil;
import com.example.h.cloudcycle.WebServiceControl.SignupResponse;

import java.io.File;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends Activity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.input_reEnterPassword)
    EditText _repeatedPasswordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;
    @BindView(R.id.userImage)

    ImageView userImage;

    int IMG_REQUEST = 555;
    String name;
    String email;
    String password;
    String repeatedPassword;
    MultipartBody.Part theImage = null;
    Uri imageURI;
    SignupResponse signupResponse;
    Call<SignupResponse> call;
    private ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
    private String imagePath;

    public static String getRealPathFromURI(Context context, Uri uri) {

        String filePath = "";

        String[] column = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, null, null, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void signup() {

        if (!HelperClass.isNetworkAvailable(this)) {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_LONG).show();
            return;
        }
        if (!validate()) {

            onSignupFailed();
            return;
        }

        // _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        name = _nameText.getText().toString().trim();
        email = _emailText.getText().toString().trim();
        password = _passwordText.getText().toString().trim();
        repeatedPassword = _repeatedPasswordText.getText().toString().trim();

        if (imagePath != null) {
            File file = new File(imagePath);

            RequestBody imagePart = RequestBody.create(MediaType.parse(getContentResolver().getType(imageURI)), file);

            theImage = MultipartBody.Part.createFormData("img", file.getName(), imagePart);

            call = apiInterface.createNewUser(name, email, password, theImage, "mobileApp", "bicloud_App2018#@");

        } else {

            call = apiInterface.createNewUser(name, email, password, "mobileApp", "bicloud_App2018#@");
        }

        call.enqueue(new Callback<SignupResponse>() {

            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {


                signupResponse = response.body();

                Toast.makeText(SignupActivity.this, signupResponse.getSuccess().toString(), Toast.LENGTH_SHORT).show();

                if (response.isSuccessful()) {

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {

                                    if (signupResponse.getSuccess()[0].equals("true"))
                                        onSignupSuccess();
                                    else if (signupResponse.getEmail() != null)
                                        _emailText.setError("Repeated Email...");
                                    else if (signupResponse.getEmail() != null)
                                        _emailText.setError("between 6 and 32 alphanumeric characters");
                                    else if (signupResponse.getImg() != null)
                                        _emailText.setError("Image Error...");

                                    progressDialog.dismiss();
                                }
                            }, 1000);

                    Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_SHORT).show();
                }
                //   Log.d("Response Token: ", signupResponse.getToken());

            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Log.d("Response:", t.getMessage());
            }
        });

    }

    public void selectImage(View view) {

        HelperClass.askPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, 2);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {

            imageURI = data.getData();
            userImage.setImageURI(imageURI);

            //    imagePath = getRealPathFromURI(this, imageURI);

            imagePath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());

            Toast.makeText(this, "Image Path: " + imagePath, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Image URI: " + imageURI.toString().trim(), Toast.LENGTH_SHORT).show();

        }
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent intent = new Intent(getApplicationContext(), EdgeActivity.class);
        finish();
        startActivity(intent);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign-Up Failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {

        boolean valid = true;

        String name = _nameText.getText().toString().trim();
        String email = _emailText.getText().toString().trim();
        password = _passwordText.getText().toString().trim();
        repeatedPassword = _repeatedPasswordText.getText().toString().trim();

        boolean isNameValid = Pattern.matches("^[a-zA-Z_ ]*$", name);

        if (name.isEmpty() || name.length() < 5) {

            _nameText.setError("at least 5 characters");
            valid = false;

        } else if (!isNameValid) {

            _nameText.setError("Enter valid name contains letters only");
            valid = false;

        } else {

            _nameText.setError(null);

        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            _emailText.setError("enter a valid email address");
            valid = false;

        } else {

            _emailText.setError(null);

        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 32) {

            _passwordText.setError("between 6 and 32 alphanumeric characters");
            valid = false;

        } else if (!password.equals(repeatedPassword)) {

            _passwordText.setError("Password Dose Not Match");
            _repeatedPasswordText.setError("Password Dose Not Match");
            valid = false;

        } else {
            _passwordText.setError(null);
            _repeatedPasswordText.setError(null);
        }

        return valid;
    }
}