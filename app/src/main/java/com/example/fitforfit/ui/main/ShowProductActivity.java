package com.example.fitforfit.ui.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Product;
import com.example.fitforfit.singleton.Database;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class ShowProductActivity extends AppCompatActivity {

    Product prod;
    int prodId;
    int mealId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        Log.d("CHECKPOINT", "0");
        //auslesen porudct id aus intent
        //ausfüllen felder
        //add button über gabe an add ingredient to meal acitivty
        getProduct();
        initViews();
    }

    private void getProduct() {
        if(getIntent().hasExtra("prodId")) {
            Log.d("CHECKPOINT", "1");
            String prodIdS = getIntent().getStringExtra("prodId");
            this.prodId = Integer.valueOf(prodIdS);
            AppDatabase db = Database.getInstance(this);
            this.prod = db.productDao().getProductById(this.prodId);
            Log.d("CHECKPOINT", "2");
        }
    }

    private void initViews() {
        if(getIntent().hasExtra("mealId") && getIntent().getStringExtra("mealId") != ""){
            String mealIdS = getIntent().getStringExtra("mealId");
            this.mealId = Integer.valueOf(mealIdS);
        }


        TextView prodNameText = findViewById(R.id.DateValue);
        TextView ckalText = findViewById(R.id.ckalValue);
        TextView fatText = findViewById(R.id.fatValue);
        TextView satfatText = findViewById(R.id.satfatValue);
        TextView carbText = findViewById(R.id.carbValue);
        TextView sugarText = findViewById(R.id.sugarValue);
        TextView fiberText = findViewById(R.id.fiberValue);
        TextView proteinText = findViewById(R.id.proteinValue);
        TextView saltText = findViewById(R.id.saltValue);
        TextView infoText = findViewById(R.id.infoValue);

        Button addButton = findViewById(R.id.RemoveIngredientButton);
        Button cancelButton = findViewById(R.id.cancelIngredientButton);

        prodNameText.setText(this.prod.product_name);
        ckalText.setText(String.valueOf(this.prod.ckal));
        fatText.setText(String.valueOf(this.prod.fat));
        satfatText.setText(String.valueOf(this.prod.saturated_fat));
        carbText.setText(String.valueOf(this.prod.carb));
        sugarText.setText(String.valueOf(this.prod.sugar));
        fiberText.setText(String.valueOf(this.prod.fiber));
        proteinText.setText(String.valueOf(this.prod.protein));
        saltText.setText(String.valueOf(this.prod.salt));
        infoText.setText(this.prod.info);

        addButton.setOnClickListener(view -> {
            AppDatabase db = Database.getInstance(this);
            int mId = 0;
            if(String.valueOf(this.mealId) != null && String.valueOf(this.mealId) != ""){
                mId = this.mealId;
            }else{
                mId = db.mealDao().getLastMealId();
            }


            Intent intent = new Intent(this, AddIngredientToMealActivity.class);
            intent.putExtra("prodId", String.valueOf(this.prod.id));
            intent.putExtra("mealId", String.valueOf(mId));
            this.startActivity(intent);
            finish();
        });
        cancelButton.setOnClickListener(view -> {
            AppDatabase db = Database.getInstance(this);
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Bestätigen")
                    .setMessage("Dieses Produkt wirklich löschen?")
                    .setPositiveButton("JA", null)
                    .setNegativeButton("NEIN", null)
                    .show();

            Button positiveBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveBtn.setOnClickListener(view1 -> {
                db.productDao().deleteProductById(this.prodId);
                finish();

            });

        });

    }
}