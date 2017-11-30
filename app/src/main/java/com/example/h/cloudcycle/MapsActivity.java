package com.example.h.cloudcycle;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private Marker myMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney;
        // Add a marker in Sydney and move the camera
        float[][] locations = new float[2][2];
        locations[0][0] = (float) 30.758227;
        locations[0][1] = (float) 32.244555;
        locations[1][0] = (float) 30.758227;
        locations[1][1] = (float) 32.244000;
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j < 1; j++) {
                sydney = new LatLng(locations[i][j], locations[i][j + 1]);
                mMap.addMarker(new MarkerOptions().position(sydney).title("#100"+(i+1))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bicycle_icon)));
            }
        }
        sydney = new LatLng(30.808227, 32.244774);
        mMap.addMarker(new MarkerOptions().position(sydney).title("#1003").icon(BitmapDescriptorFactory.fromResource(R.drawable.bicycle_icon)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 5.0f));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker m) {
                Intent intent = new Intent(getApplicationContext(), BicycleLocation.class);
                intent.putExtra("location", m.getPosition().toString());
                Toast.makeText(MapsActivity.this, m.getPosition().toString(), Toast.LENGTH_SHORT).show();
                intent.putExtra("code", m.getTitle());
                startActivity(intent);
                return true;
            }
        });


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }
}
