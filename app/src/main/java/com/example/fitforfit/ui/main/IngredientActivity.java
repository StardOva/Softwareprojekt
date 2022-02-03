package com.example.fitforfit.ui.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Ingredient;
import com.example.fitforfit.entity.Product;
import com.example.fitforfit.singleton.Database;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class IngredientActivity extends AppCompatActivity {

    AppDatabase db;

    private int ingId;
    private Ingredient ing;
    private Product prod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        getDbInstance();
        getIngredientId();
        getIngredient();
        getProduct();

        initViews();
    }

    private void initViews() {

        //Zutatanzeige mit Werten pro zugenommene Menge

        TextView ingNameText = findViewById(R.id.IngredientNameTextView);
        TextView ingquantityText = findViewById(R.id.IngredientquantityTextView);
        TextView ingckalText = findViewById(R.id.ingckalValue);
        TextView ingfatText = findViewById(R.id.ingfatValue);
        TextView ingsatfatText = findViewById(R.id.ingsatfatValue);
        TextView ingcarbText = findViewById(R.id.ingcarbValue);
        TextView ingsugarText = findViewById(R.id.ingsugarValue);
        TextView ingfiberText = findViewById(R.id.ingfiberValue);
        TextView ingproteinText = findViewById(R.id.ingproteinValue);
        TextView ingsaltText = findViewById(R.id.ingsaltValue);

        ingNameText.setText(this.ing.ingredient_name);
        ingquantityText.setText(String.valueOf(this.ing.quantity)+"g");
        float quant = this.ing.quantity;
        float quantFactor = quant / 100;

        DecimalFormat round = new DecimalFormat("#.##");

        Log.d("CHECK_VALUE_QUANTFACTOR",String.valueOf(quantFactor));
        ingckalText.setText(String.valueOf(round.format(this.prod.ckal * quantFactor)));
        ingfatText.setText(String.valueOf(round.format(this.prod.fat * quantFactor)));
        ingsatfatText.setText(String.valueOf(round.format(this.prod.saturated_fat * quantFactor)));
        ingcarbText.setText(String.valueOf(round.format(this.prod.carb * quantFactor)));
        ingsugarText.setText(String.valueOf(round.format(this.prod.sugar * quantFactor)));
        ingfiberText.setText(String.valueOf(round.format(this.prod.fiber * quantFactor)));
        ingproteinText.setText(String.valueOf(round.format(this.prod.protein * quantFactor)));
        ingsaltText.setText(String.valueOf(round.format(this.prod.salt * quantFactor)));


        //Produktanzeige mit Werten pro 100g

        TextView prodNameText = findViewById(R.id.DateValue);
        TextView prodckalText = findViewById(R.id.ckalValue);
        TextView prodfatText = findViewById(R.id.fatValue);
        TextView prodsatfatText = findViewById(R.id.satfatValue);
        TextView prodcarbText = findViewById(R.id.carbValue);
        TextView prodsugarText = findViewById(R.id.sugarValue);
        TextView prodfiberText = findViewById(R.id.fiberValue);
        TextView prodproteinText = findViewById(R.id.proteinValue);
        TextView prodsaltText = findViewById(R.id.saltValue);
        TextView prodinfoText = findViewById(R.id.infoValue);

        prodNameText.setText(round.format(this.prod.product_name));
        prodckalText.setText(String.valueOf(round.format(this.prod.ckal)));
        prodfatText.setText(String.valueOf(round.format(this.prod.fat)));
        prodsatfatText.setText(String.valueOf(round.format(this.prod.saturated_fat)));
        prodcarbText.setText(String.valueOf(round.format(this.prod.carb)));
        prodsugarText.setText(String.valueOf(round.format(this.prod.sugar)));
        prodfiberText.setText(String.valueOf(round.format(this.prod.fiber)));
        prodproteinText.setText(String.valueOf(round.format(this.prod.protein)));
        prodsaltText.setText(String.valueOf(round.format(this.prod.salt)));
        prodinfoText.setText(round.format(this.prod.info));


        Button removeBtn = findViewById(R.id.RemoveIngredientButton);
        removeBtn.setOnClickListener(view -> {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Bestätigen")
                    .setMessage("Diese Zutat wirklich löschen?")
                    .setPositiveButton("JA", null)
                    .setNegativeButton("NEIN", null)
                    .show();

            Button positiveBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveBtn.setOnClickListener(view1 -> {
                db.ingredientDao().deleteIngredientById(this.ingId);
                finish();
            });

        });

        Button backBtn = findViewById(R.id.cancelIngredientButton);
        backBtn.setOnClickListener(view -> {finish();});

        Button changeBtn = findViewById(R.id.changeIngredientButton);
        changeBtn.setOnClickListener(view -> {

            Intent intent = new Intent(this, ChangeIngredientActivity.class);
            intent.putExtra("ingId", String.valueOf(this.ingId));
            this.startActivity(intent);
        });
    }

    private void getProduct() {
        int prodId = db.ingredientDao().selectProdId(this.ingId);
        this.prod = db.productDao().getProductById(prodId);
    }

    private void getDbInstance() {
        this.db = Database.getInstance(this);
    }

    private void getIngredient() {
         this.ing = db.ingredientDao().getIngredientById(this.ingId);
    }

    private void getIngredientId() {
        String ingIdS = getIntent().getStringExtra("ingId");
        this.ingId = Integer.valueOf(ingIdS);
        Log.d("INGREDIENT_ID", String.valueOf(ingId));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getIngredient();
        initViews();

    }
}