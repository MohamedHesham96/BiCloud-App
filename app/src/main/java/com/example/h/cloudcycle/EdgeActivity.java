package com.example.h.cloudcycle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class EdgeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edge);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get User Data... ID, Email, Username.....
        getSharedPreferences();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MapFragment mapFragment = new MapFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainLayout, mapFragment).commit();
    }

    public void getSharedPreferences() {


        SharedPreferences sp = this.getSharedPreferences("Login", MODE_PRIVATE);

        String emailSP = sp.getString("email", null);
        String passwordSP = sp.getString("password", null);
        String nameSP = sp.getString("name", null);

        // still there strings here... like (id.. balance....)

        if (!sp.equals(null)) {

            if (!emailSP.equals("") && !passwordSP.equals("")) {

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

                View hView = navigationView.getHeaderView(0);
                View sView = navigationView.getHeaderView(1);

                TextView nav_user = (TextView) hView.findViewById(R.id.userEmail);
                TextView name_user = (TextView) hView.findViewById(R.id.userName);

                nav_user.setText(emailSP);
                name_user.setText(nameSP);
            }
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView userNameTV = (TextView) findViewById(R.id.userName);
        TextView userEmailTV = (TextView) findViewById(R.id.userEmail);

        if (requestCode == 0) {

            userNameTV.setText(getIntent().getStringExtra("name"));
            userEmailTV.setText(getIntent().getStringExtra("email"));
        }
    }
}
