package com.nicanoritorma.mynotes.intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.nicanoritorma.mynotes.LoginActivity;
import com.nicanoritorma.mynotes.MainActivity;
import com.nicanoritorma.mynotes.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();

        SharedPreferences preferences = getSharedPreferences("first_time", MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean("FIRST_TIME", true);

        Log.d("Is first time", Boolean.toString(isFirstTime));
        if (isFirstTime) {
            handler.postDelayed(() -> startActivity(new Intent(getApplicationContext(), IntroActivity.class)), 1500);
        } else {
            preferences = getSharedPreferences("PIN", 0);
            String pin_isOn = preferences.getString("LOGIN_PIN", "");
            String fingerprint_isOn = preferences.getString("LOGIN_FINGERPRINT", "");
            String pin = preferences.getString("pin", "");

            if (pin_isOn.equals("true") || fingerprint_isOn.equals("true")) {
                if (!pin.isEmpty()) {
                    handler.postDelayed(() -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)), 1500);
                }
            } else {
                handler.postDelayed(() -> startActivity(new Intent(getApplicationContext(), MainActivity.class)), 1500);
            }

        }
    }

    @Override
    public void onBackPressed() {
        // Does nothing, you have to see the intro!
    }
}