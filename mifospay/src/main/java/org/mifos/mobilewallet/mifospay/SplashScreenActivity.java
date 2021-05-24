package org.mifos.mobilewallet.mifospay;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import org.mifos.mobilewallet.mifospay.createuser.ui.OnBoardingActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private final int splashScreenDuration = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, OnBoardingActivity.class));
                finish();
            }
        }, splashScreenDuration);
    }
}
