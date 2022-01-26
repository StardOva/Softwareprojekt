package com.example.fitforfit.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Ingredient;
import com.example.fitforfit.singleton.Database;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class AddIngredientToMealActivity extends AppCompatActivity {

    int mealId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient_to_meal);

        initViews();
    }

    private void initViews() {

        String mealIdS = getIntent().getStringExtra("mealId");
        this.mealId = Integer.valueOf(mealIdS);

        Button test = findViewById(R.id.test);
        Log.d("MEALID:",mealIdS);
        test.setOnClickListener((view -> {

            AppDatabase db = Database.getInstance(this);
            Ingredient testi = new Ingredient();
            testi.meal_id = this.mealId;
            testi.ingredient_name = "Zucker";
            testi.product_id = 187;
            testi.quantity = 23;

            db.ingredientDao().insert(testi);

            finish();

        }));
    }
}