package com.example.fitforfit.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Meal;
import com.example.fitforfit.singleton.Database;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class MealActivity extends AppCompatActivity {

    int mealId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        /*
        TODO intent -> meal_id von Button bekommen
        -> alle ingredients dazu anzeigen
        -> meal bearbeiten button

        TODO zurück zu TrackerDayActivity mit Intent der dayId
         */

        initViews();
    }

    private void initViews() {
        String mealIdS = getIntent().getStringExtra("mealId");
        this.mealId = Integer.valueOf(mealIdS);

        Button deleteButton = findViewById(R.id.deletebutton);
        deleteButton.setText("Remove Meal");
        deleteButton.setOnClickListener((v -> {

            AppDatabase db = Database.getInstance(this);
            db.mealDao().deleteMealById(mealId);
            finish();

        }));
    }
}