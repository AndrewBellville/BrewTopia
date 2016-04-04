package com.town.small.brewtopia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

/**
 * Created by Andrew on 4/3/2016.
 */
public class SplashActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}
