package com.example.h.cloudcycle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.h.cloudcycle.WebServiceControl.ApiClient;
import com.example.h.cloudcycle.WebServiceControl.ApiInterface;
import com.example.h.cloudcycle.WebServiceControl.Bike;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final int REQUEST_LOCATION_CODE = 99;
    List<Bike> bikes;
    double latitude, longitude;
    private FusedLocationProviderClient client2;
    private ApiInterface apiInterface;
    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private Marker currentLocationmMarker;

    private List<MarkerOptions> markerOptionsList;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getContext( fragment

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }
        return inflater.inflate(R.layout.fragment_blank, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1);
        supportMapFragment.getMapAsync(this);


    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (client == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;

        Button button = this.getActivity().findViewById(R.id.refresh);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reloadBikes(mMap);
            }
        });

        button.callOnClick();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

        }
    }

    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false;

        } else
            return true;
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public void reloadBikes(final GoogleMap mMap) {

        Toast.makeText(getContext(), "Refresh", Toast.LENGTH_SHORT).show();

        SharedPreferences sp = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);

        final String userType = sp.getString("type", null);

        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<List<Bike>> call;


        if (userType.equals("user")) {

            call = apiInterface.getLockedBikes("mobileApp", "bicloud_App2018#@");

        } else {

            call = apiInterface.getAllBikes("mobileApp", "bicloud_App2018#@");
        }

        call.enqueue(new Callback<List<Bike>>() {
            @Override
            public void onResponse(Call<List<Bike>> call, Response<List<Bike>> response) {
                bikes = response.body();

                mMap.clear();

                for (Bike b : bikes) {
                    LatLng latLng = new LatLng(b.getLatitude(), b.getLongitude());
                    MarkerOptions markerOption = new MarkerOptions();
                    markerOption.position(latLng);

                    if (userType.equals("user"))
                        markerOption.title("Id: " + b.getId() + " Name: " + b.getName());

                    else {

                        markerOption.title("Bike Id: " + b.getId() + " Name: " + b.getName() + " User Id: " + b.getUser_id());
                    }

                    markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_icon_64));
                    currentLocationmMarker = mMap.addMarker(markerOption);

                }

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                    @Override
                    public boolean onMarkerClick(final Marker marker) {
                        Toast.makeText(getContext(), "Bike clickec", Toast.LENGTH_SHORT).show();

                        final Intent intent = new Intent(getActivity(), BikeActivity.class);
                        intent.putExtra("bikeDetail", marker.getTitle());

                        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                        if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        }

                        client2 = LocationServices.getFusedLocationProviderClient(getActivity());

                        client2.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {

                            @Override
                            public void onSuccess(Location location) {

                                if (location != null) {

                                    double longitude = location.getLongitude();
                                    double latitude = location.getLatitude();

//                                      double dis = CalculationByDistance(marker.getPosition(), latLng1);

                                    float[] results = new float[1];
                                    Location.distanceBetween(latitude, longitude, marker.getPosition().latitude, marker.getPosition().longitude, results);
                                    float distanceInMeters = results[0];
                                    boolean isWithin10km = distanceInMeters < 2;

                                    if (isWithin10km) {

                                        startActivity(intent);
                                    } else {

                                        Toast.makeText(getContext(), "Be closer to bike", Toast.LENGTH_LONG).show();

                                    }
                                }

                            }
                        });

                        return false;
                    }
                });

            }

            @Override
            public void onFailure(Call<List<Bike>> call, Throwable t) {


            }
        });

    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {//Can add more as per requirement

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }


    private class LoadBikes extends AsyncTask<GoogleMap, Void, GoogleMap> {

        boolean flag = true;

        @Override
        protected GoogleMap doInBackground(GoogleMap... GoogleMap) {


            Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();

            mMap = GoogleMap[0];

            SharedPreferences sp = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);

            String userType = sp.getString("type", null);

            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

            Call<List<Bike>> call;


            mMap.clear();

            if (userType.equals("usesr")) {

                call = apiInterface.getLockedBikes("mobileApp", "bicloud_App2018#@");

            } else {

                call = apiInterface.getAllBikes("mobileApp", "bicloud_App2018#@");
            }

            call.enqueue(new Callback<List<Bike>>() {
                @Override
                public void onResponse(Call<List<Bike>> call, Response<List<Bike>> response) {
                    bikes = response.body();

                    for (Bike b : bikes) {
                        LatLng latLng = new LatLng(b.getLatitude(), b.getLongitude());
                        MarkerOptions markerOption = new MarkerOptions();
                        markerOption.position(latLng);
                        //    Toast.makeText(getContext(), b.getName(), Toast.LENGTH_SHORT).show();
                        markerOption.title("Id: " + b.getId() + " Name: " + b.getName());
                        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_icon_64));
                        currentLocationmMarker = mMap.addMarker(markerOption);

                    }

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            Intent intent = new Intent(getActivity(), BikeActivity.class);
                            intent.putExtra("code", marker.getTitle());

                            return false;
                        }
                    });


                }

                @Override
                public void onFailure(Call<List<Bike>> call, Throwable t) {


                }
            });

            // this.onPostExecute(mMap);
            return mMap;
        }

        @Override
        protected void onPostExecute(GoogleMap googleMap) {


            if (flag) {

                doInBackground(googleMap);

            } else {


            }

        }
    }


}
