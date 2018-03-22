package com.example.h.cloudcycle.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by H on 21/03/2018.
 */

public class HelperClass {

    public static void askPermission(Context context, String permission, int requestCode) {

        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
            Toast.makeText(context, "Permission is Not Granted", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(context, "Permission is Already Granted", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
