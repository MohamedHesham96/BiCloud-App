package com.example.h.cloudcycle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h.cloudcycle.WebServiceControl.ApiClient;
import com.example.h.cloudcycle.WebServiceControl.ApiInterface;
import com.example.h.cloudcycle.WebServiceControl.GeneralResponse;

import java.io.File;

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

    @BindView(R.id.update_name_email)
    Button updateNameEmail_BT;

    @BindView(R.id.user_name)
    TextView userName_TV;

    @BindView(R.id.user_email)
    TextView userEmail_TV;

    @BindView(R.id.user_password)
    TextView userPassword_TV;

    @BindView(R.id.upload_photo)
    Button uploadPhoto_BT;

    int IMG_REQUEST = 555;

    String idSP;
    String nameSP;
    String emailSP;
    String passwordSP;

    String imagePath;
    MultipartBody.Part theImage = null;
    Uri imageURI;

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

        SharedPreferences sp = this.getSharedPreferences("Login", MODE_PRIVATE);

        idSP = sp.getString("id", null);
        nameSP = sp.getString("name", null);
        emailSP = sp.getString("email", null);
        passwordSP = sp.getString("password", null);

        userName_TV.setText(nameSP);
        userEmail_TV.setText(emailSP);
        userPassword_TV.setText(passwordSP);

        Toast.makeText(this, idSP, Toast.LENGTH_SHORT).show();


    }

    public void checkSharedPreferences() {

        SharedPreferences sp = this.getSharedPreferences("Login", MODE_PRIVATE);

        idSP = sp.getString("id", null);
        nameSP = sp.getString("name", null);
        emailSP = sp.getString("email", null);
        passwordSP = sp.getString("password", null);

        userName_TV.setText(nameSP);
        userEmail_TV.setText(emailSP);
        userPassword_TV.setText(passwordSP);

    }

    public void updateNameAndEmail(View view) {

        Intent intent = new Intent();
        intent.putExtra("userName", userName_TV.getText().toString());
        intent.putExtra("userEmail", userEmail_TV.getText().toString());

        intent.setClass(this, UpdateNameAndEmailActivity.class);
        startActivity(intent);
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

            Toast.makeText(this, imageURI.toString(), Toast.LENGTH_SHORT).show();

            userPhoto_IV.setImageURI(imageURI);

            imagePath = getRealPathFromURI(this, imageURI);

            uploadPhoto_BT.setVisibility(View.VISIBLE);

        }
    }

    public void uploadPhoto(View view) {

        if (imagePath != null) {
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
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {

            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
