package com.example.fitforfit.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.example.fitforfit.MainActivity;
import com.example.fitforfit.R;
import com.example.fitforfit.sync.DatabaseSync;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initToolbar() {
        initToolbar("");
    }

    protected void initToolbar(String addToAppName) {
        Toolbar toolbar = findViewById(R.id.mainToolbar);
        if (!addToAppName.equals("")) {
            toolbar.setTitle(getString(R.string.app_name) + " - " + addToAppName);
        }

        toolbar.setNavigationIcon(R.drawable.ic_action_back);

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_options_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("abc", "Fick dich App");
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.license:
                Intent intent1 = new Intent(this, LicenseActivity.class);
                startActivity(intent1);
                return true;
            case R.id.aboutUs:
                Intent intent2 = new Intent(this, AboutUsActivity.class);
                startActivity(intent2);
                return true;
            case R.id.downloadDb:
                DatabaseSync.downloadDB(getApplicationContext(), DatabaseSync.getBackupUrl(getApplicationContext()));
                return true;
            case R.id.uploadDb:
                Log.d("abc", "Halt da maul");
                DatabaseSync.uploadDB(getApplicationContext(), DatabaseSync.getBackupUrl(getApplicationContext()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
