package com.example.fitforfit.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.fitforfit.R;
import com.example.fitforfit.adapter.IngredientListAdapter;
import com.example.fitforfit.adapter.MealListAdapter;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Ingredient;
import com.example.fitforfit.entity.Meal;
import com.example.fitforfit.entity.Workout;
import com.example.fitforfit.singleton.Database;

import java.util.List;

public class AddNewMealActivity extends AppCompatActivity {

    int dayId;
    List<Meal> mealList;
    private IngredientListAdapter ingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_meal);

        initViews();
        cleanProducts();
        initRecyclerView();// zeigt alle Zutaten des meals
        loadIngredientList();



    }

    private void cleanProducts() {
            AppDatabase db = Database.getInstance(this);
            db.productDao().cleanProducts();
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

        Button addIngredient = findViewById(R.id.addIngToMealButton);
        addIngredient.setText("+Ingredient");
        addIngredient.setOnClickListener((v -> {
            //AddIngredientToMealActivity öffnen
            Intent intent = new Intent(this, AddIngredientToMealActivity.class);
            intent.putExtra("mealId", String.valueOf(db.mealDao().getLastMealId()));
            this.startActivity(intent);
        }));

        Button addButton = findViewById(R.id.addButton);
        addButton.setEnabled(false);
        addButton.setText("Add");
        addButton.setOnClickListener((v -> {
            String name = mealNameText.getText().toString();
            if(name == null || name == "" || name.matches("")){
                name = "Meal#" + mealCount+1;
            }
            Log.d("MealAdded", "Added Meal on Day: " + this.dayId);
            //TODO auto timestamp
            db.mealDao().updateMealNameTimeByMealId(name, "18:34", db.mealDao().getLastMealId());

            finish();

        }));
        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setText("Cancel");
        cancelButton.setOnClickListener((v -> {

            db.mealDao().deleteMealById(db.mealDao().getLastMealId());
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

    private void initRecyclerView() {
        AsyncTask.execute(() -> {
            RecyclerView recyclerViewMeals = findViewById(R.id.recyclerViewMeals);
            recyclerViewMeals.setLayoutManager(new LinearLayoutManager(this));

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            recyclerViewMeals.addItemDecoration(dividerItemDecoration);
            ingListAdapter = new IngredientListAdapter(this);
            recyclerViewMeals.setAdapter(ingListAdapter);
        });
    }
    private void loadIngredientList() {

        AppDatabase db = Database.getInstance(this);
        //mealID id des zukünftigen MEals
        int mealId = db.mealDao().getLastMealId();
        List<Ingredient> ingredientList = db.ingredientDao().getAllIngredientsOnMeal(mealId);
        ingListAdapter.setContext(this);
        ingListAdapter.setIngredientList(ingredientList);
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

    @Override
    protected void onResume() {
        super.onResume();
        loadIngredientList();
    }

}