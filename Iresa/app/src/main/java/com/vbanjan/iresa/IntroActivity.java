package com.vbanjan.iresa;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.vbanjan.iresa.Fragment.WalkThroughFragment01;
import com.vbanjan.iresa.Fragment.WalkThroughFragment02;
import com.vbanjan.iresa.Fragment.WalkThroughFragment03;

public class IntroActivity extends AppIntro2 {

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpIntro();
    }

    public void askPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                Toast.makeText(this, "App requires camera to function properly", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            }
        } else {
            finish();
        }
    }

    public void setUpIntro() {
        showSkipButton(false);
        setBarColor(Color.GRAY);
        setVibrate(true);
        setVibrateIntensity(30);
        addSlide(new WalkThroughFragment01()); //We're here/ at restaurant/bar
        addSlide(new WalkThroughFragment02()); //Scan the magic code
        addSlide(new WalkThroughFragment03()); //vote for songs
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onDonePressed(Fragment currentFragment) {
        askPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
