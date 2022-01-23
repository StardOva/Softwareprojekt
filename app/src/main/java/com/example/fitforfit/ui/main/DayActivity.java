package com.example.fitforfit.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Day;
import com.example.fitforfit.singleton.Database;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class DayActivity extends AppCompatActivity {

    TextView dateText;
    Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        initViews();
    }

    private void initViews() {

        String date = getIntent().getStringExtra("date");

        AppDatabase db = Database.getInstance(this);
        int dayId = db.dayDao().getIdByDate(date);

        dateText = findViewById(R.id.date_text);
        dateText.setText(date + " ID:" + String.valueOf(dayId));

        returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(view -> onReturnButtonClick());

    }

    private void onReturnButtonClick() {
        finish();
    }
}