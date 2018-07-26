package com.android.facefilter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private Handler mHandler = new Handler();
    private  Runnable mRunnable;
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callActivity();

                    }

                }, SPLASH_TIME_OUT);
            }

            private void callActivity() {

                    startActivity(new Intent(SplashScreen.this,FaceFilterActivity.class));
                    finish();
                }


            @Override
            protected void onDestroy() {
                if (mHandler != null && mRunnable != null){
                    mHandler.removeCallbacks(mRunnable);
                }

                super.onDestroy();
            }
        }
