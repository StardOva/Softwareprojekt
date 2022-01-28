package com.example.fitforfit.ui.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Ingredient;
import com.example.fitforfit.entity.Meal;
import com.example.fitforfit.entity.Product;
import com.example.fitforfit.singleton.Database;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddIngredientToMealActivity extends AppCompatActivity {

    int mealId;
    int productId;
    Product product;

    TextView productNameText;
    EditText amountEditText;
    Button manuellButton;
    Button scanButton;
    Button searchButton;
    Button addIngButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient_to_meal);



        initViews();

    }



    private void initViews() {

        this.scanButton = findViewById(R.id.scanButton);
        this.searchButton = findViewById(R.id.searchButton);

        this.manuellButton = findViewById(R.id.manuellButton);
        manuellButton.setText("Eingabe");
        manuellButton.setOnClickListener(view -> {
            AppDatabase db = Database.getInstance(this);
            Product newprod = new Product();
            newprod.product_name = "NO_PLACEHOLDER";
            db.productDao().insert(newprod);


            Intent intent = new Intent(this, AddNewIngredientManuellProduct.class);
            this.startActivityForResult(intent, 0);
            //this.startActivity(intent);

        });

        this.addIngButton = findViewById(R.id.addIngButton);
        this.addIngButton.setText("Add");
        this.addIngButton.setEnabled(false);

        Button cancelIngButton = findViewById(R.id.cancelIngButton);
        cancelIngButton.setText("Cancel");
        cancelIngButton.setOnClickListener(view -> {
            //AppDatabase db = Database.getInstance(this);
            //db.productDao().deleteProductById(db.productDao().getLastProductId());
            cleanProducts();
            finish();
        });

        this.amountEditText = findViewById(R.id.amountEditText);
        boolean amountIsSet = TextUtils.isDigitsOnly(this.amountEditText.getText());

        this.productNameText = findViewById(R.id.productNameText);
        //Intent von AddNewIngredientManuellProduct id des Prodcut ->db suche per product ID

        String mealIdS = getIntent().getStringExtra("mealId");
        this.mealId = Integer.valueOf(mealIdS);

        /*Button test = findViewById(R.id.test);
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

        }));*/
    }

    private void cleanProducts() {

        AppDatabase db = Database.getInstance(this);
        db.productDao().cleanProducts();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == RESULT_OK && data != null && data.hasExtra("lastProdId")){
            this.productId = Integer.parseInt(data.getStringExtra("lastProdId"));
            AppDatabase db = Database.getInstance(this);
            this.product = db.productDao().findById(productId);

            this.manuellButton.setEnabled(false);
            this.searchButton.setEnabled(false);
            this.scanButton.setEnabled(false);

            this.amountEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String textFieldString = charSequence.toString().trim();

                    // bei Länge 0
                    if (textFieldString.length() == 0) {
                        addIngButton.setEnabled(false);
                        return;
                    }

                    // ansonsten den Button aktivieren
                    addIngButton.setEnabled(true);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            //activ wenn Amount eingegeben
            //this.addIngButton.setEnabled(true);
            this.addIngButton.setOnClickListener(view -> {

                Ingredient newingredient = new Ingredient();
                newingredient.ingredient_name = productNameText.getText().toString();
                newingredient.product_id = productId;
                newingredient.meal_id = this.mealId;
                newingredient.quantity = Integer.parseInt(amountEditText.getText().toString());

                db.ingredientDao().insert(newingredient);
                //ingredient mit productId, ingrdeitnnam=productname, mealid und quantity(amount) hinzufügen
                finish();

            });

            this.productNameText.setText(product.product_name);
            Log.d("ProductId:", String.valueOf(productId));
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        cleanProducts();
    }
}