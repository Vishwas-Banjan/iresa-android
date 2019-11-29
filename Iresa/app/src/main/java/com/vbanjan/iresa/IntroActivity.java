package com.vbanjan.iresa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.vbanjan.iresa.Fragment.WalkThroughFragment01;
import com.vbanjan.iresa.Fragment.WalkThroughFragment02;
import com.vbanjan.iresa.Fragment.WalkThroughFragment03;

public class IntroActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        askForPermissions(new String[]{Manifest.permission.CAMERA}, 2);
        setUpIntro();
    }

    public void setUpIntro() {
        showSkipButton(false);
        setBarColor(Color.GRAY);
        addSlide(new WalkThroughFragment01()); //We're here/ at restaurant/bar
        addSlide(new WalkThroughFragment02()); //Scan the magic code
        addSlide(new WalkThroughFragment03()); //vote for songs
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please check out what we can do!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        finish();
    }

}
