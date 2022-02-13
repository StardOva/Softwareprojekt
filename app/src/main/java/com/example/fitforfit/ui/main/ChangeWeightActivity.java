package com.example.fitforfit.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.singleton.Database;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeWeightActivity extends AppCompatActivity {

    int dayId;
    AppDatabase db = Database.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_weight);
        initData();
        EditText weightText = findViewById(R.id.weightEditText);
        Button changeBtn = findViewById(R.id.saveWeightBtn);


        changeBtn.setOnClickListener(view -> {
            try {
                db.dayDao().updateWeightById(Float.parseFloat(String.valueOf(weightText.getText())), this.dayId);
                finish();
            }catch (NumberFormatException n){
                String message = "Bitte gebe nur Zahlen ein";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                //Log.d("TEST2", dayText + " " + monthYearFromDate(selectedDate));
            }


        });
    }

    private void initData() {
        this.dayId = db.dayDao().getLastDayId();
    }
}