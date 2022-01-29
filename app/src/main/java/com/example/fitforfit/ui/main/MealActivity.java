package com.example.fitforfit.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.adapter.IngredientListAdapter;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Ingredient;
import com.example.fitforfit.entity.Meal;
import com.example.fitforfit.singleton.Database;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.util.List;

public class MealActivity extends AppCompatActivity {

    int mealId;
    private IngredientListAdapter ingListAdapter;

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
        initRecyclerView();// zeigt alle Zutaten des meals
        //loadIngredientList();
    }

    private void loadIngredientList() {
        AppDatabase db = Database.getInstance(this);

        List<Ingredient> ingredientList = db.ingredientDao().getAllIngredientsOnMeal(this.mealId);
        ingListAdapter.setContext(this);
        ingListAdapter.setIngredientList(ingredientList);
    }

    private void initRecyclerView() {
        AsyncTask.execute(() -> {
            RecyclerView recyclerViewMeals = findViewById(R.id.recyclerViewMeals);
            recyclerViewMeals.setLayoutManager(new LinearLayoutManager(this));

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            recyclerViewMeals.addItemDecoration(dividerItemDecoration);
            ingListAdapter = new IngredientListAdapter(this);
            recyclerViewMeals.setAdapter(ingListAdapter);

            //TODO not efficient - content of loadIngredients()
            AppDatabase db = Database.getInstance(this);

            List<Ingredient> ingredientList = db.ingredientDao().getAllIngredientsOnMeal(this.mealId);
            ingListAdapter.setContext(this);
            ingListAdapter.setIngredientList(ingredientList);
        });
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