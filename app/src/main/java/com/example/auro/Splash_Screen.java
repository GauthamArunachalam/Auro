package com.example.auro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash_Screen extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public String username, designation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);
        designation = prefs.getString("Designation",null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(username != null){
                    startActivity(new Intent(getApplicationContext(),HomePage.class));
                    finish();
                }else{
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }

            }
        },2500);
    }
}
