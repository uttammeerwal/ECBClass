package com.ecbclass.user_activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.ecbclass.database.MainActivity;
import com.google.firebase.quickstart.database.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final ProgressBar splashProgress = (ProgressBar) findViewById(R.id.splashProgress);
        final Handler handler1 = new Handler();

        for (int i = 0; i <= 3; i++) {
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    splashProgress.setVisibility(View.VISIBLE);

                }
            }, 1000 * i);
        }
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                intentForMain();
            }
        }, 3000);
    }

    void intentForMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
