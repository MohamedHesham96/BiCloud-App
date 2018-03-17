package com.example.h.cloudcycle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h.cloudcycle.WebServiceControl.ApiClient;
import com.example.h.cloudcycle.WebServiceControl.ApiInterface;
import com.example.h.cloudcycle.WebServiceControl.GeneralResponse;
import com.example.h.cloudcycle.WebServiceControl.RealPathUtil;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.user_photo)
    CircleImageView userPhoto_IV;

    @BindView(R.id.select_photo)
    ImageView updatePhoto;

    @BindView(R.id.update_name)
    Button updateName_BT;

    @BindView(R.id.update_email)
    Button updateEmail_BT;

    @BindView(R.id.cancel_update_email)
    Button cancelUpdateEmail_BT;

    @BindView(R.id.cancel_update_name)
    Button cancelUpdateName_BT;

    @BindView(R.id.upload_photo)
    Button uploadPhoto_BT;

    @BindView(R.id.user_name)
    TextView userName_ET;

    @BindView(R.id.user_email)
    TextView userEmail_ET;

    @BindView(R.id.user_password)
    TextView userPassword_TV;


    int IMG_REQUEST = 555;

    String idSP;
    String nameSP;
    String emailSP;
    String passwordSP;
    String IMAGES_PATH = "https://mousaelenanyfciscu.000webhostapp.com/public/images/";

    String imagePath;
    MultipartBody.Part theImage = null;
    Uri imageURI;

    SharedPreferences sp;
    boolean updateNameClockedFlag = false;
    boolean updateEmailClockedFlag = false;
    boolean tryToUpdateUserEmail = false;

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
    protected void onRestart() {
        super.onRestart();

        checkSharedPreferences();

        Toast.makeText(this, "hello i'm restart state !!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("My Profile");
        setupOnTextChangeEvents();

        sp = getSharedPreferences("Login", MODE_PRIVATE);

        userName_ET.setEnabled(false);
        userEmail_ET.setEnabled(false);

        SharedPreferences sp = this.getSharedPreferences("Login", MODE_PRIVATE);

        String imageName = sp.getString("image", null);

        String fullPath = IMAGES_PATH + imageName;

        Toast.makeText(this, fullPath, Toast.LENGTH_SHORT).show();

        new DownLoadImageTask(userPhoto_IV).execute(fullPath);

        Toast.makeText(this, idSP, Toast.LENGTH_SHORT).show();

        idSP = sp.getString("id", null);
        nameSP = sp.getString("name", null);
        emailSP = sp.getString("email", null);
        passwordSP = sp.getString("password", null);

        userName_ET.setText(nameSP);
        userEmail_ET.setText(emailSP);
        userPassword_TV.setText(passwordSP);
    }

    public void setupOnTextChangeEvents() {

        userName_ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (updateNameClockedFlag) {
                    updateName_BT.setText("Done");
                    cancelUpdateName_BT.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

                updateNameClockedFlag = false;
            }
        });

        userEmail_ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (updateEmailClockedFlag) {

                    updateEmail_BT.setText("Done");
                    cancelUpdateEmail_BT.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

                updateEmailClockedFlag = false;
            }
        });
    }

    public void userNameUpdate(String userId, final String userName) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<GeneralResponse> call = apiInterface.updateUserName(userId, userName);

        call.enqueue(new Callback<GeneralResponse>() {

            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                if (response.isSuccessful()) {

                    GeneralResponse generalResponse = response.body();

                    if (generalResponse.isSuccess()) {
                        userName_ET.setEnabled(false);
                        updateName_BT.setText("Edit");
                        updateNameClockedFlag = false;
                        cancelUpdateName_BT.setVisibility(View.INVISIBLE);

                        SharedPreferences.Editor Ed = sp.edit();
                        Ed.putString("name", userName);
                        Ed.commit();
                        Toast.makeText(ProfileActivity.this, userName, Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });

    }

    public void updateUserName(View view) {

        updateNameClockedFlag = true;

        if (!userName_ET.isEnabled()) {

            userName_ET.setEnabled(true);
            cancelUpdateName_BT.setVisibility(View.VISIBLE);

        } else {

            String userNameValue = userName_ET.getText().toString();

            boolean isNameValid = Pattern.matches("^[a-zA-Z_ ]*$", userNameValue);

            if (userNameValue.isEmpty() || userNameValue.length() < 3) {

                userName_ET.setError("at least 3 characters");
                return;

            } else if (!isNameValid) {

                userName_ET.setError("Enter Valid Username");
                return;

            } else if (userNameValue.equals(nameSP)) {

                Toast.makeText(this, "Enter New Username", Toast.LENGTH_SHORT).show();
                return;

            } else {

                userName_ET.setError(null);
            }

            if (!userNameValue.equals(sp.getString("name", null)))
                userNameUpdate(idSP, userNameValue);

            else {

                Toast.makeText(this, "Enter New Name", Toast.LENGTH_SHORT).show();
            }


        }

    }

    public void updateEmailUpdate(View view) {

        updateEmailClockedFlag = true;

        if (!userEmail_ET.isEnabled()) {
            userEmail_ET.setEnabled(true);
            cancelUpdateEmail_BT.setVisibility(View.VISIBLE);
        }

        String userEmailValue = userEmail_ET.getText().toString();

        boolean isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(userEmailValue).matches();

        if (userEmailValue.isEmpty() || !isEmailValid) {

            userEmail_ET.setError("Enter a valid email address");
            return;

        } else {

            userEmail_ET.setError(null);
        }


        Toast.makeText(this, "TRy: " + tryToUpdateUserEmail, Toast.LENGTH_SHORT).show();

        if (!userEmailValue.equals(sp.getString("email", null)))
            userEmailUpdate(idSP, userEmailValue);
        else {

            Toast.makeText(this, "Enter New Email", Toast.LENGTH_SHORT).show();
        }
    }

    public void userEmailUpdate(String userId, final String userEmail) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<GeneralResponse> call = apiInterface.updateUserEmail(userId, userEmail);

        call.enqueue(new Callback<GeneralResponse>() {

            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                updateEmailClockedFlag = true;
                tryToUpdateUserEmail = true;

                if (response.isSuccessful()) {

                    GeneralResponse generalResponse = response.body();

                    if (generalResponse.isSuccess()) {
                        userEmail_ET.setEnabled(false);
                        updateEmail_BT.setText("Edit");
                        cancelUpdateEmail_BT.setVisibility(View.INVISIBLE);

                        SharedPreferences.Editor Ed = sp.edit();
                        Ed.putString("email", userEmail);
                        Ed.commit();

                        Toast.makeText(ProfileActivity.this, userEmail, Toast.LENGTH_SHORT).show();

                    } else {

                        userEmail_ET.setError("Duplicate  Email !");
                    }

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });

    }

    public void checkSharedPreferences() {


        SharedPreferences sp = this.getSharedPreferences("Login", MODE_PRIVATE);

        idSP = sp.getString("id", null);
        nameSP = sp.getString("name", null);
        emailSP = sp.getString("email", null);
        passwordSP = sp.getString("password", null);

        userName_ET.setText(nameSP);
        userEmail_ET.setText(emailSP);
        userPassword_TV.setText(passwordSP);

    }

    public void cancelAnUpdate(View view) {

        if (view.getId() == R.id.cancel_update_name) {

            updateNameClockedFlag = false;
            updateName_BT.setText("Edit");
            userName_ET.setText(sp.getString("name", null));
            userName_ET.setEnabled(false);
            view.setVisibility(View.INVISIBLE);

        } else {

            updateEmailClockedFlag = false;
            updateEmail_BT.setText("Edit");
            userEmail_ET.setText(sp.getString("email", null));
            userEmail_ET.setEnabled(false);
            view.setVisibility(View.INVISIBLE);

        }

    }

    public void selectImage(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);

    }

    public void uploadPhoto(View view) {

        if (imagePath != null) {

            uploadPhoto_BT.setEnabled(false);

            File file = new File(imagePath);

            RequestBody imagePart = RequestBody.create(MediaType.parse(getContentResolver().getType(imageURI)), file);

            theImage = MultipartBody.Part.createFormData("img", file.getName(), imagePart);

            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

            Call<GeneralResponse> call = apiInterface.updateUserPhoto(idSP, theImage);

            call.enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                    if (response.isSuccessful()) {

                        GeneralResponse gr = response.body();
                        Toast.makeText(ProfileActivity.this, "Photo: " + String.valueOf(gr.isSuccess()), Toast.LENGTH_SHORT).show();
                        uploadPhoto_BT.setVisibility(View.INVISIBLE);

                    } else {

                        Toast.makeText(ProfileActivity.this, "Photo: " + "Error !!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ProfileActivity.this, "Photo: " + response.code(), Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {


                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {

            imageURI = data.getData();

            Toast.makeText(this, imageURI.toString(), Toast.LENGTH_SHORT).show();

            userPhoto_IV.setImageURI(imageURI);

            imagePath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());

            Toast.makeText(this, "imagePath: " + imagePath, Toast.LENGTH_SHORT).show();

            uploadPhoto_BT.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {

            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateNameAndEmail(View view) {

        Intent intent = new Intent();
        intent.putExtra("userName", userEmail_ET.getText().toString());
        intent.putExtra("userEmail", userEmail_ET.getText().toString());

        intent.setClass(this, UpdateNameAndEmailActivity.class);
        startActivity(intent);
    }

    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {

        CircleImageView imageView;

        public DownLoadImageTask(CircleImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e) { // Catch the download exception
                e.printStackTrace();
                Log.d("Error: Profile", "Prooooooooooooooofile Iamge");
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

    }

}

