package com.example.fitforfit.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Ingredient;
import com.example.fitforfit.entity.Product;
import com.example.fitforfit.singleton.Database;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        Log.d("CHECK_VALUE_QUANTFACTOR",String.valueOf(quantFactor));
        ingckalText.setText(String.valueOf(this.prod.ckal * quantFactor));
        ingfatText.setText(String.valueOf(this.prod.fat * quantFactor));
        ingsatfatText.setText(String.valueOf(this.prod.saturated_fat * quantFactor));
        ingcarbText.setText(String.valueOf(this.prod.carb * quantFactor));
        ingsugarText.setText(String.valueOf(this.prod.sugar * quantFactor));
        ingfiberText.setText(String.valueOf(this.prod.fiber * quantFactor));
        ingproteinText.setText(String.valueOf(this.prod.protein * quantFactor));
        ingsaltText.setText(String.valueOf(this.prod.salt * quantFactor));


        //Produktanzeige mit Werten pro 100g

        TextView prodNameText = findViewById(R.id.ProductNameValue);
        TextView prodckalText = findViewById(R.id.ckalValue);
        TextView prodfatText = findViewById(R.id.fatValue);
        TextView prodsatfatText = findViewById(R.id.satfatValue);
        TextView prodcarbText = findViewById(R.id.carbValue);
        TextView prodsugarText = findViewById(R.id.sugarValue);
        TextView prodfiberText = findViewById(R.id.fiberValue);
        TextView prodproteinText = findViewById(R.id.proteinValue);
        TextView prodsaltText = findViewById(R.id.saltValue);
        TextView prodinfoText = findViewById(R.id.infoValue);

        prodNameText.setText(this.prod.product_name);
        prodckalText.setText(String.valueOf(this.prod.ckal));
        prodfatText.setText(String.valueOf(this.prod.fat));
        prodsatfatText.setText(String.valueOf(this.prod.saturated_fat));
        prodcarbText.setText(String.valueOf(this.prod.carb));
        prodsugarText.setText(String.valueOf(this.prod.sugar));
        prodfiberText.setText(String.valueOf(this.prod.fiber));
        prodproteinText.setText(String.valueOf(this.prod.protein));
        prodsaltText.setText(String.valueOf(this.prod.salt));
        prodinfoText.setText(this.prod.info);
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
}