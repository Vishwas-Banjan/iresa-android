package com.vbanjan.iresa;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.vbanjan.iresa.Fragment.ScanCodeFragment;
import com.vbanjan.iresa.Fragment.StoreDetailsFragment;
import com.vbanjan.iresa.Model.Store;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements LocationListener,
        ScanCodeFragment.onScanCodeFragmentListener,
        StoreDetailsFragment.onStoreDetailsFragment {

    private static final String TAG = "demo";
    NavController navController;
    LocationManager locationManager;
    Store store = null;
    int locationMinTime = 600000;
    int locationMinDistance = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            final Intent i = new Intent(MainActivity.this, IntroActivity.class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startActivity(i);
                }
            });

            //  Make a new preferences editor
            SharedPreferences.Editor e = getPrefs.edit();

            //  Edit preference to make it false because we don't want this to run again
            e.putBoolean("firstStart", false);
            e.putString("uniqueID", String.valueOf(UUID.randomUUID()));
            //  Apply changes
            e.apply();
        } else {
            //Permission requested on AppIntro
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, MainActivity.this);
            navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
            if (navController.getCurrentDestination().getId() == R.id.songsListFragment ||
                    navController.getCurrentDestination().getId() == R.id.storeDetailsFragment) {
                super.onResume();
            } else {
                navController
                        .navigate(R.id.scanCodeFragment,
                                null,
                                new NavOptions.Builder()
                                        .setPopUpTo(navController.getGraph().getId(),
                                                true)
                                        .build());
            }
        }
        super.onResume();
    }

    @Override
    public void navigateScanCodeToStoreDetail(Bundle bundle) {
        navController.navigate(R.id.action_scanCodeFragment_to_storeDetailsFragment, bundle);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocation Changed: " + location);
        if (store != null) {
            Location currentLocation = new Location("");
            currentLocation.setLatitude(store.getStoreLat());
            currentLocation.setLongitude(store.getStoreLng());
            Log.d(TAG, "onLocationChanged: " + currentLocation.distanceTo(location));
            if (currentLocation.distanceTo(location) >= 1000) { // If store is not within 1000m of current location
                store = null;
                if (navController.getCurrentDestination().getId() != R.id.scanCodeFragment) {
                    openScanCodeFragment();
                }
            }
        }
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
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "Status: " + status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled: ");
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }


    @Override
    public void getStoreDetails(Store store) {
        this.store = store;
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
}
