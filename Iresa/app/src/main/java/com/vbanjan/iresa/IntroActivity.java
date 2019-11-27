package com.vbanjan.iresa;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.vbanjan.iresa.Fragment.WalkThroughFragment01;
import com.vbanjan.iresa.Fragment.WalkThroughFragment02;
import com.vbanjan.iresa.Fragment.WalkThroughFragment03;

public class IntroActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpIntro();
    }

    public void setUpIntro() {
        showSkipButton(false);
        setBarColor(Color.GRAY);
        addSlide(new WalkThroughFragment01());
        addSlide(new WalkThroughFragment02());
        addSlide(new WalkThroughFragment03());
    }
}
