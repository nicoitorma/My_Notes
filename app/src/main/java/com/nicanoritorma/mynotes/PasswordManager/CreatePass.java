package com.nicanoritorma.mynotes.PasswordManager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.nicanoritorma.mynotes.LoginActivity;
import com.nicanoritorma.mynotes.R;

public class CreatePass extends AppCompatActivity {

    private TextInputEditText et_pin1, et_pin2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pass);

        et_pin1 = findViewById(R.id.et_reset_password1);
        et_pin2 = findViewById(R.id.et_reset_password2);
        Button reset_pass = findViewById(R.id.btn_reset_pass);

        //password length handler
        et_pin1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && s.length() < 4)
                {
                    et_pin1.setError("Password must be at least 4 characters");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        reset_pass.setOnClickListener(v -> {
            String pin1 = et_pin1.getText().toString().trim();
            String pin2 = et_pin2.getText().toString().trim();
            checkValues(pin1, pin2);
        });
    }

    private void checkValues(String pin1, String pin2)
    {
        if (pin1.equals(pin2))
        {
            changePin();
        }
        else
        {
            et_pin2.setError("PIN do not match");
        }
    }

    private void changePin()
    {
        SharedPreferences preferences = getSharedPreferences("PIN", MODE_PRIVATE);
        SharedPreferences.Editor editor;

        editor = preferences.edit();
        editor.putString("pin", et_pin2.getText().toString().trim());
        editor.apply();
        finish();
    }
}