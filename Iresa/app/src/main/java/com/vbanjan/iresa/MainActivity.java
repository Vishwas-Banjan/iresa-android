package com.vbanjan.iresa;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.vbanjan.iresa.Fragment.ScanCodeFragment;
import com.vbanjan.iresa.Fragment.StoreDetailsFragment;
import com.vbanjan.iresa.Model.Store;

import java.util.UUID;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MainActivity extends AppCompatActivity implements
        ScanCodeFragment.onScanCodeFragmentListener,
        StoreDetailsFragment.onStoreDetailsFragment {

    private static final String TAG = "demo";
    NavController navController;
    Store store = null;
    private LocationRequest mLocationRequest;
    int locationMinTime = 10 * 60000; //10 minutes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        boolean isFirstStart = getPrefs.getBoolean("firstStart", true);
        //  If the activity has never started before...
        if (isFirstStart ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_DENIED)) {
            //  Launch app intro
            Intent i = new Intent(MainActivity.this, IntroActivity.class);
            startActivity(i);

            //  Make a new preferences editor
            SharedPreferences.Editor e = getPrefs.edit();

            //  Edit preference to make it false because we don't want this to run again
            e.putBoolean("firstStart", false);
            e.putString("uniqueID", String.valueOf(UUID.randomUUID()));
            //  Apply changes
            e.apply();
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { //Check if GPS Enabled
                buildAlertMessageNoGps();
            } else {
                startLocationUpdates(); //Get and compare user's location
                navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
                if (navController.getCurrentDestination().getId() != R.id.songsListFragment &&
                        navController.getCurrentDestination().getId() != R.id.storeDetailsFragment) {
                    navController
                            .navigate(R.id.scanCodeFragment,
                                    null,
                                    new NavOptions.Builder()
                                            .setPopUpTo(navController.getGraph().getId(),
                                                    true)
                                            .build());
                }
            }
        }
        super.onResume();
    }

    @Override
    public void navigateScanCodeToStoreDetail(Bundle bundle) {
        navController.navigate(R.id.action_scanCodeFragment_to_storeDetailsFragment, bundle);
    }

    public void openScanCodeFragment() {
        navController
                .navigate(R.id.scanCodeFragment,
                        null,
                        new NavOptions.Builder()
                                .setPopUpTo(navController.getGraph().getId(),
                                        true)
                                .build());
    }

    @Override
    public void getStoreDetails(Store store) {
        this.store = store;
    }

    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(locationMinTime);
        mLocationRequest.setFastestInterval(0);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " + location.getLatitude() + "," + location.getLongitude();
        Log.d(TAG, "onLocationChanged: " + msg);
        if (store != null) {
            Location currentLocation = new Location("");
            currentLocation.setLatitude(store.getStoreLat());
            currentLocation.setLongitude(store.getStoreLng());
            Log.d(TAG, "onLocationChanged: " + currentLocation.distanceTo(location));
            if (currentLocation.distanceTo(location) >= 1000) { // If store is not within 1000m of current location
                buildAlertMessageNotInStore();
            }
        }
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        Toast.makeText(MainActivity.this, "Goodbye!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void buildAlertMessageNotInStore() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You seem to be too far, scan QR Code again?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        store = null;
                        if (navController.getCurrentDestination().getId() != R.id.scanCodeFragment) {
                            openScanCodeFragment();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        Toast.makeText(MainActivity.this, "Goodbye!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        if (!alert.isShowing()) alert.show();
    }
}
