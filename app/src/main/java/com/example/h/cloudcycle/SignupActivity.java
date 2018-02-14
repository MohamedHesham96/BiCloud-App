package com.example.h.cloudcycle;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h.cloudcycle.WebServiceControl.ApiClient;
import com.example.h.cloudcycle.WebServiceControl.ApiInterface;
import com.example.h.cloudcycle.WebServiceControl.SignupResponse;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_name)
    EditText _nameText;
    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.link_login)
    TextView _loginLink;
    @Bind(R.id.userImage)
    ImageView userImage;

    MultipartBody.Part theImage;
    int IMG_REQUEST = 555;
    private ApiInterface apiInterface;

    private Bitmap bitmap;
    String name;
    String email;
    String password;
    SignupResponse signupResponse;
    RequestBody filePart;
    Uri imageURI;
    private String imagePath;

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
            }
        });
    }

    public void signup() {
        if (!validate()) {

            onSignupFailed();
            return;
        }

        //   _signupButton.setEnabled(false);

    /*    final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.Theme_AppCompat_DayNight);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();*/
        //signupRequest();

        name = _nameText.getText().toString();
        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();

        File file = new File(imagePath);

        RequestBody imagePart = RequestBody.create(MediaType.parse(getContentResolver().getType(imageURI)), file);

        MultipartBody.Part image = MultipartBody.Part.createFormData("img", file.getName(), imagePart);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<SignupResponse> call = apiInterface.createNewUser(name, email, password, image);

        call.enqueue(new Callback<SignupResponse>() {

            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {

                signupResponse = response.body();

                Toast.makeText(SignupActivity.this, signupResponse.getSuccess().toString(), Toast.LENGTH_SHORT).show();

                if (response.isSuccessful()) {

                    if (signupResponse.getSuccess()[0].equals("true"))
                        onSignupSuccess();
                    else if (signupResponse.getEmail() != null)
                        _emailText.setError("Repeated Email...");
                    else if (signupResponse.getEmail() != null)
                        _emailText.setError("between 6 and 32 alphanumeric characters");
                    else if (signupResponse.getImg() != null)
                        _emailText.setError("Image Error...");
                }
                //   Log.d("Response Token: ", signupResponse.getToken());

                Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Log.d("Response Token Errir: ", t.getCause().toString());

            }
        });

    }

    public void selectImage(View view) {

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
            imagePath = getRealPathFromURI_API19(this, imageURI);

            userImage.setImageURI(imageURI);
            //  Toast.makeText(this, "Image Path: "  + imagePath, Toast.LENGTH_SHORT).show();

            // Toast.makeText(this, imagePath, Toast.LENGTH_SHORT).show();
        }
    }

    //   @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
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

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent intent = new Intent(getApplicationContext(), EdgeActivity.class);
        finish();
        startActivity(intent);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
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
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}