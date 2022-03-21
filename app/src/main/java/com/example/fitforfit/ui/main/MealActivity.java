package com.example.fitforfit.ui.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.adapter.IngredientListAdapter;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Ingredient;
import com.example.fitforfit.singleton.Database;

import java.util.List;

public class MealActivity extends BaseActivity {

    int mealId;
    private IngredientListAdapter ingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        initToolbar();

        /*
        TODO intent -> meal_id von Button bekommen
        -> alle ingredients dazu anzeigen
        -> meal bearbeiten button

        TODO zurück zu TrackerDayActivity mit Intent der dayId
         */

        initViews();
        initRecyclerView();// zeigt alle Zutaten des meals
        //loadIngredientList();
    }

    private void loadIngredientList() {
        AppDatabase db = Database.getInstance(this);

        List<Ingredient> ingredientList = db.ingredientDao().getAllIngredientsOnMeal(this.mealId);
        //ingListAdapter.setContext(this);
        //ingListAdapter.setIngredientList(ingredientList);
        this.runOnUiThread(() -> {
            ingListAdapter.setContext(this);
            ingListAdapter.setIngredientList(ingredientList);
        });
    }

    private void initRecyclerView() {
        //AsyncTask.execute(() -> {
            RecyclerView recyclerViewMeals = findViewById(R.id.recyclerViewMeals);
            recyclerViewMeals.setLayoutManager(new LinearLayoutManager(this));

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            recyclerViewMeals.addItemDecoration(dividerItemDecoration);
            ingListAdapter = new IngredientListAdapter(this);
            recyclerViewMeals.setAdapter(ingListAdapter);

            AsyncTask.execute(this::loadIngredientList);
            //TODO not efficient - content of loadIngredients()
        /*
            AppDatabase db = Database.getInstance(this);

            List<Ingredient> ingredientList = db.ingredientDao().getAllIngredientsOnMeal(this.mealId);
            ingListAdapter.setContext(this);
            ingListAdapter.setIngredientList(ingredientList);
            Log.d("CHECKPOINT", "1");*/
       // });
    }

    private void initViews() {
        String mealIdS = getIntent().getStringExtra("mealId");
        this.mealId = Integer.valueOf(mealIdS);

        Button addIngredient = findViewById(R.id.addbutton);
        //addIngredient.setText("+Ingredient");
        addIngredient.setOnClickListener((v -> {
            //AddIngredientToMealActivity öffnen
            Intent intent = new Intent(this, AddIngredientToMealActivity.class);
            intent.putExtra("mealId", String.valueOf(this.mealId));
            this.startActivity(intent);
        }));
        Button deleteButton = findViewById(R.id.deletebutton);
        //deleteButton.setText("Remove Meal");
        deleteButton.setOnClickListener((v -> {

            AppDatabase db = Database.getInstance(this);

            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Bestätigen")
                    .setMessage("Diese Mahlzeit wirklich löschen?")
                    .setPositiveButton("JA", null)
                    .setNegativeButton("NEIN", null)
                    .show();

            Button positiveBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveBtn.setOnClickListener(view1 -> {
                db.mealDao().deleteMealById(mealId);
                db.ingredientDao().deleteAllIngredientByMealId(mealId);
                finish();

            });
        }));







    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
    }
}