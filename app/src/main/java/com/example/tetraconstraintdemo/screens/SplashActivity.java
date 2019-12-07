package com.example.tetraconstraintdemo.screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tetraconstraintdemo.R;
import com.example.tetraconstraintdemo.managers.LoginManager;

public class SplashActivity extends AppCompatActivity {


    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (LoginManager.getInstance().isLoggedIn()) {
                    Intent i = new Intent(SplashActivity.this, MapsViewActivity.class);
                    startActivity(i);

                    finish();
                } else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);

                    finish();
                }


            }
        }, 1000);
    }

    @Override
    protected void onPause() {

        handler.removeCallbacksAndMessages(null);


        super.onPause();
    }


}
