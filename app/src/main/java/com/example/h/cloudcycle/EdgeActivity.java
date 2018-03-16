package com.example.h.cloudcycle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h.cloudcycle.WebServiceControl.ApiClient;
import com.example.h.cloudcycle.WebServiceControl.ApiInterface;
import com.example.h.cloudcycle.WebServiceControl.User;

import java.io.InputStream;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EdgeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String IMAGES_PATH = "https://mousaelenanyfciscu.000webhostapp.com/public/images/";
    private NavigationView navigationView;

    @Override
    protected void onRestart() {
        super.onRestart();

        getSharedPreferences();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edge);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");


        //Get User Data... Image, ID, Email, Username
        getSharedPreferences();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MapFragment mapFragment = new MapFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainLayout, mapFragment).commit();

    }

    public void getSharedPreferences() {

        final NavigationView navigationView = findViewById(R.id.nav_view);
        final View hView = navigationView.getHeaderView(0);

        final TextView nav_user = hView.findViewById(R.id.userEmail);
        final TextView name_user = hView.findViewById(R.id.userName);
        final ImageView profileImage = hView.findViewById(R.id.profileImage);

        SharedPreferences sp = this.getSharedPreferences("Login", MODE_PRIVATE);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> call = apiInterface.getUserInfo(sp.getString("email", null), sp.getString("password", null));

        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                User user = response.body();

                if (response.isSuccessful()) {

                    SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                    SharedPreferences.Editor Ed = sp.edit();

                    Ed.putString("id", String.valueOf(user.getId()));
                    Ed.putString("name", user.getName());
                    Ed.putString("email", user.getEmail());
                    Ed.putString("image", user.getImage());
                    Ed.putString("type", user.getType());
                    Ed.putString("balance", user.getBalance());

                    String fullPath = IMAGES_PATH + user.getImage();

                    Toast.makeText(EdgeActivity.this, "fullpath: " + fullPath, Toast.LENGTH_SHORT).show();

                    new DownLoadImageTask(profileImage).execute(fullPath);
                    nav_user.setText(user.getEmail());
                    name_user.setText(user.getName());

                    Toast.makeText(EdgeActivity.this, user.getImage(), Toast.LENGTH_SHORT).show();
                    Ed.commit();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


        // still there strings here... like (id.. balance....)

        /*if (sp != null) {

            String emailSP = sp.getString("email", null);
            String passwordSP = sp.getString("password", null);
            String nameSP = sp.getString("name", null);
            String idSP = sp.getString("id", null);

            Toast.makeText(this, "USer ID:" + idSP, Toast.LENGTH_SHORT).show();
            if (sp.contains("email")) {
                if (!emailSP.equals("") && !passwordSP.equals("")) {

                    NavigationView navigationView = findViewById(R.id.nav_view);
                    View hView = navigationView.getHeaderView(0);

                    TextView nav_user = hView.findViewById(R.id.userEmail);
                    TextView name_user = hView.findViewById(R.id.userName);
                    ImageView profileImage = hView.findViewById(R.id.profileImage);

                    String imageName = sp.getString("image", null);

                    String fullPath = IMAGES_PATH + imageName;

                    Toast.makeText(this, "fullpath: " + fullPath, Toast.LENGTH_SHORT).show();

                    new DownLoadImageTask(profileImage).execute(fullPath);
                    nav_user.setText(emailSP);
                    name_user.setText(nameSP);
                }


            }
        }*/
    }

    public Bitmap getBitmapFromURL(String src) {

        try {
            InputStream in = new URL(src).openStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(in);
            return myBitmap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.history) {

            startActivity(new Intent(this, HistoryActivity.class));

        } else if (id == R.id.logout) {

            startActivity(new Intent(this, LoginActivity.class));
            SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
            SharedPreferences.Editor Ed = sp.edit();
            Ed.putString("email", "");
            Ed.putString("image", "");
            Ed.putString("password", "");
            Ed.putString("id", "");
            Ed.putString("name", "");
            Ed.putString("balance", "");
            Ed.commit();

        } else if (id == R.id.payment) {

            startActivity(new Intent(this, PaymentActivity.class));

        } else if (id == R.id.profile) {

            startActivity(new Intent(this, ProfileActivity.class));

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

/*
        ImageView profileImage = (ImageView) findViewById(R.id.profileImage);

        Toast.makeText(this, getSharedPreferences("Login", MODE_PRIVATE).getString("image", null), Toast.LENGTH_SHORT).show();
        if (requestCode == 0) {


        }*/
    }

    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView) {
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

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
