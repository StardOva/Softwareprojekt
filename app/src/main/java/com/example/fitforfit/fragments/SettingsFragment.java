package com.example.fitforfit.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.fitforfit.R;
import com.example.fitforfit.sync.DatabaseSync;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        EditTextPreference timerCountdown = findPreference("timer_length_preference");

        if (timerCountdown != null) {
            timerCountdown.setOnBindEditTextListener(editText -> {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            });
        }

        EditTextPreference backup_url = findPreference("backup_url");

        if (backup_url != null) {
            backup_url.setOnBindEditTextListener(editText -> {
                editText.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE | InputType.TYPE_CLASS_TEXT);
            });
        }

        EditTextPreference api_key = findPreference("api_key");

        if (api_key != null) {
            api_key.setOnBindEditTextListener(editText -> {
                editText.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE | InputType.TYPE_CLASS_TEXT);
            });
        }
    }
}
