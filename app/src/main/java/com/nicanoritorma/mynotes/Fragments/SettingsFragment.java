package com.nicanoritorma.mynotes.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.nicanoritorma.mynotes.PasswordManager.CreatePass;
import com.nicanoritorma.mynotes.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String pin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int xmlId = R.xml.root_preferences;
        addPreferencesFromResource(xmlId);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }


    @Override
    public void onResume() {
        super.onResume();

        preferences = getActivity().getSharedPreferences("PIN", Context.MODE_PRIVATE);
        pin = preferences.getString("pin", "");
        if (!pin.isEmpty())
        {
            Preference pin_login = findPreference("pin_settings");
            if (pin_login != null)
            {
                pin_login.setEnabled(true);
            }
        }

        Preference addPin = findPreference("add_pin");
        if (addPin != null)
        {
            addPin.setOnPreferenceClickListener(preference -> {
                startActivity(new Intent(getContext(), CreatePass.class));
                return false;
            });
        }

        SwitchPreferenceCompat pin_settings = findPreference("pin_settings");
        if (pin_settings != null)
        {
            pin_settings.setOnPreferenceChangeListener((preference, newValue) -> {
                pin_settings.setChecked((Boolean) newValue);
                editor = preferences.edit();
                editor.putString("LOGIN_PIN", newValue.toString());
                editor.apply();
                return false;
            });
        }

        SwitchPreferenceCompat fingerprint = findPreference("fingerprint_settings");
        if (fingerprint != null)
        {
            fingerprint.setOnPreferenceChangeListener((preference, newValue) -> {
                if (haveFingerprint())
                {
                    fingerprint.setChecked((Boolean) newValue);
                    editor = preferences.edit();
                    editor.putString("LOGIN_FINGERPRINT", newValue.toString());
                    editor.apply();
                }
                else
                {
                    Toast.makeText(getContext(), "No fingerprint enrolled", Toast.LENGTH_SHORT).show();
                }
                return false;
            });
        }
    }

    private boolean haveFingerprint()
    {
        FingerprintManager fingerprintManager = (FingerprintManager) getActivity().getSystemService(Context.FINGERPRINT_SERVICE);
        if (fingerprintManager.hasEnrolledFingerprints())
        {
            return true;
        }
        return false;
    }
}