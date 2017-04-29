package com.mi1.duitku;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }

        }, 500);
    }
}
