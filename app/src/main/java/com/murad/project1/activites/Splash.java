package com.murad.project1.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.murad.project1.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();


    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            Intent i=new Intent(Splash.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }, 3000);
    }
}
