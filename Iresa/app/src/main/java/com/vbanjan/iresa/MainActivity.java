package com.vbanjan.iresa;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "demo";
    NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        //  Declare a new thread to do a preference and permission check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);
                //  If the activity has never started before...
                if (isFirstStart || (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
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
                    navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
                    navController.navigate(R.id.action_welcomeFragment_to_scanCodeFragment);

                }
            }
        });
        // Start the thread
        t.start();

        super.onResume();
    }
}
