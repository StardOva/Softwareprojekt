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

        EditTextPreference backup = findPreference("backup");

        if (backup != null) {
            backup.setOnBindEditTextListener(editText -> {
                editText.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE | InputType.TYPE_CLASS_TEXT);
            });

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
            String urlString = sharedPrefs.getString("backup", "localhost");
            DatabaseSync.uploadDB(this.getContext(), urlString);
        }

        EditTextPreference backupDown = findPreference("backup_down");

        if (backup != null) {
            backupDown.setOnBindEditTextListener(editText -> {
                editText.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE | InputType.TYPE_CLASS_TEXT);
            });

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
            String urlString = sharedPrefs.getString("backup_down", "localhost");
            DatabaseSync.downloadDB(this.getContext(), urlString);
        }
    }
}
