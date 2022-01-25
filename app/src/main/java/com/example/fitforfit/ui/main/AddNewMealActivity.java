package com.example.fitforfit.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Meal;
import com.example.fitforfit.entity.Workout;
import com.example.fitforfit.singleton.Database;

import java.util.List;

public class AddNewMealActivity extends AppCompatActivity {

    int dayId;
    List<Meal> mealList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_meal);

        initViews();

    }

    private void initViews() {
        String dayIdS = getIntent().getStringExtra("dayId");
        this.dayId = Integer.valueOf(dayIdS);
        AppDatabase db = Database.getInstance(this);
        String date = db.dayDao().getDateById(this.dayId);
        this.mealList = db.mealDao().getAllMealsOnDay(this.dayId);
        int mealCount = this.mealList.size();

        TextView day = findViewById(R.id.textday);
        day.setText(date);

        TextView mealName = findViewById(R.id.textmeal);
        mealName.setText("Name:");

        EditText mealNameText = findViewById(R.id.mealNameEditText);
        mealNameText.setText("");

        Button addButton = findViewById(R.id.addButton);
        addButton.setEnabled(false);
        addButton.setText("Add");
        addButton.setOnClickListener((v -> {
            String name = mealNameText.getText().toString();
            if(name == null || name == "" || name.matches("")){
                name = "Meal#" + mealCount+1;
            }
            Log.d("MealAdded", "Added Meal on Day: " + this.dayId);
            Meal newmeal = new Meal();
            newmeal.meal_name = name;
            //TODO automatic timestamp
            newmeal.time = "18:34";//timestap.tostring;
            newmeal.day_id = this.dayId;
            db.mealDao().insert(newmeal);

            finish();

        }));
        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setText("Cancel");
        cancelButton.setOnClickListener((v -> {

            finish();

        }));

        // Speichern soll nur möglich sein, wenn der Workoutname noch nicht existiert und nicht leer ist
        mealNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String textFieldString = charSequence.toString().trim();

                // bei Länge 0
                if (textFieldString.length() == 0) {
                    addButton.setEnabled(false);
                    return;
                }

                // prüfe, ob das Workout bereits existiert
                // TODO Hinweis an den Nutzer geben, irgendeine Art von Fehlermeldung,
                // TODO damit er weiß warum er den Button nicht drücken kann
                for (Meal meal : mealList) {
                    if (textFieldString.equalsIgnoreCase(meal.meal_name)) {
                        addButton.setEnabled(false);
                        return;
                    }
                }

                // ansonsten den Button aktivieren
                addButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /*
    String dayIdS = getIntent().getStringExtra("dayId");
    int dayId = Interger.valueof(dayIdS);
     AppDatabase db = Database.getInstance(getActivity());
            Log.d("MealAddButton", "Add Meal on Day: " + this.dayId);
            Meal newmeal = new Meal();
            newmeal.meal_name = edittext.getText();
            newmeal.time = timestap.tostring;
            newmeal.day_id = this.dayId;
            db.mealDao().insert(newmeal);
     */
}