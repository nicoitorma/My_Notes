package com.nicanoritorma.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nicanoritorma.mynotes.LoginPinDots.DotView;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv1;
    private TextView forgot_pin;
    private String password;
    private ViewGroup layout_psd;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private String fingerprint_isOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences preferences = getSharedPreferences("PIN", MODE_PRIVATE);
        fingerprint_isOn = preferences.getString("LOGIN_FINGERPRINT", "");

        if (fingerprint_isOn.equals("true"))
        {
            //check if fingerprint scanner hardware is available
            if (haveFingerprintHardware())
            {
                promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Login using fingerprint")
                        .setNegativeButtonText("Use PIN")
                        .build();
                fingerprintLogin();
                biometricPrompt.authenticate(promptInfo);
            }
        }

        password = preferences.getString("pin", "");

        tv1 = findViewById(R.id.tv1);
        tv1.setText(R.string.enter_pin);
        TextView number0 = findViewById(R.id.number0);
        TextView number1 = findViewById(R.id.number1);
        TextView number2 = findViewById(R.id.number2);
        TextView number3 = findViewById(R.id.number3);
        TextView number4 = findViewById(R.id.number4);
        TextView number5 = findViewById(R.id.number5);
        TextView number6 = findViewById(R.id.number6);
        TextView number7 = findViewById(R.id.number7);
        TextView number8 = findViewById(R.id.number8);
        TextView number9 = findViewById(R.id.number9);
        ImageView numberBack = findViewById(R.id.numberB);
        ImageView fingerprint = findViewById(R.id.fingerprint);
        layout_psd = findViewById(R.id.layout_psd);
        forgot_pin = findViewById(R.id.forgot_pin);

        number0.setOnClickListener(this);
        number1.setOnClickListener(this);
        number2.setOnClickListener(this);
        number3.setOnClickListener(this);
        number4.setOnClickListener(this);
        number5.setOnClickListener(this);
        number6.setOnClickListener(this);
        number7.setOnClickListener(this);
        number8.setOnClickListener(this);
        number9.setOnClickListener(this);
        number0.setTag(0);
        number1.setTag(1);
        number2.setTag(2);
        number3.setTag(3);
        number4.setTag(4);
        number5.setTag(5);
        number6.setTag(6);
        number7.setTag(7);
        number8.setTag(8);
        number9.setTag(9);
        numberBack.setOnClickListener(v -> deleteChar());

        //check if user added pin, if so show forgot pin function in login activity
        if (!password.isEmpty())
        {
            forgot_pin.setVisibility(View.VISIBLE);
        }

        fingerprint.setOnClickListener(v -> {
            if (fingerprint_isOn.equals("true"))
            {
                if (haveFingerprintHardware())
                {
                    fingerprintLogin();
                    biometricPrompt.authenticate(promptInfo);
                }
            }
        });

    }

    private boolean haveFingerprintHardware()
    {
        FingerprintManager fingerprintManager = (FingerprintManager) getApplicationContext().getSystemService(Context.FINGERPRINT_SERVICE);
        if (!fingerprintManager.isHardwareDetected()) {
            Toast.makeText(getApplicationContext(), "Device doesn't support fingerprint authentication", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    private void fingerprintLogin()
    {
        Executor executor = ContextCompat.getMainExecutor(getApplicationContext());
        biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.d("FINGERPRINT ERROR ", errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Authenticated successfully",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int number = (int) v.getTag();
        addChar(number);
    }

    private int dpToPx(float valueInDp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    private void addChar(int number) {
        if (layout_psd.getChildCount() >= password.length()) {
            return;
        }
        DotView psdView = new DotView(getApplicationContext());
        int size = dpToPx(10);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        params.setMargins(size, 0, size, 0);
        psdView.setLayoutParams(params);
        psdView.setColor(Color.WHITE);
        psdView.setTag(number);
        layout_psd.addView(psdView);

        String pin = getPINFromView();
        if (pin.equals(password)) {
            Handler handler = new Handler();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            handler.postDelayed(() -> {
                int childCount = layout_psd.getChildCount();
                if (childCount <= 0) {
                    return;
                }
                layout_psd.removeAllViews();

                tv1.setTextColor(Color.WHITE);
                tv1.setText(R.string.enter_pin);
            }, 900);
        }
        else if (pin.length() >= password.length() && !pin.equals(password))
        {
            runTipTextAnimation();
            tv1.setText(R.string.wrong_pin);
            tv1.setTextColor(Color.RED);
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                tv1.setTextColor(Color.WHITE);
                tv1.setText(R.string.enter_pin);
                int childCount = layout_psd.getChildCount();
                if (childCount <= 0) {
                    return;
                }
                layout_psd.removeAllViews();
            }, 900);
        }
    }

    private void deleteChar() {
        int childCount = layout_psd.getChildCount();
        if (childCount <= 0) {
            return;
        }
        layout_psd.removeViewAt(childCount - 1);
    }

    private String getPINFromView() {
        StringBuilder sb = new StringBuilder();
        int childCount = layout_psd.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = layout_psd.getChildAt(i);
            int num = (int) child.getTag();
            sb.append(num);
        }
        return sb.toString();
    }

    public void runTipTextAnimation() {
        shakeAnimator(tv1).start();
    }

    private Animator shakeAnimator(View view) {
        return ObjectAnimator
                .ofFloat(view, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                .setDuration(500);
    }
}