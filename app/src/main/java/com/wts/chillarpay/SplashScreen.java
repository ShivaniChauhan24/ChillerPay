package com.wts.chillarpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashScreen extends AppCompatActivity {

    LinearLayout descimage;
    Animation downtoup;
    ImageView imageView2;
    int forcheck;
    SharedPreferences sharedPreferences;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        descimage = findViewById(R.id.titleimage);
        imageView2 = findViewById(R.id.imageView2);
        downtoup = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        descimage.setAnimation(downtoup);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
        user=sharedPreferences.getString("userid",null);

        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(4000);

                    if (user==null)
                    {
                        Intent intent=new Intent(SplashScreen.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent intent=new Intent(SplashScreen.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}