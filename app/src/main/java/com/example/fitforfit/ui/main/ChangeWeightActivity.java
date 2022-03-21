package com.example.fitforfit.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.singleton.Database;

public class ChangeWeightActivity extends BaseActivity {

    int dayId;
    AppDatabase db = Database.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_weight);

        initToolbar(getString(R.string.tracker_name));

        initData();

        EditText weightText = findViewById(R.id.weightEditText);
        Button changeBtn = findViewById(R.id.saveWeightBtn);


        changeBtn.setOnClickListener(view -> {
            try {
                db.dayDao().updateWeightById(Float.parseFloat(String.valueOf(weightText.getText())), this.dayId);
                finish();
            } catch (NumberFormatException n) {
                String message = "Bitte gebe nur Zahlen ein";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }


        });
    }

    private void initData() {
        if (getIntent().hasExtra("dayId") && getIntent().getStringExtra("dayId") != null && getIntent().getStringExtra("dayId") != "") {
            this.dayId = Integer.parseInt(getIntent().getStringExtra("dayId"));
            Log.d("TEST", String.valueOf(dayId));
        } else {
            this.dayId = db.dayDao().getLastDayId();
        }
    }
}