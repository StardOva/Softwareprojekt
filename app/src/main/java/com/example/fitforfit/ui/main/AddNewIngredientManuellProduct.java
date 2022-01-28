package com.example.fitforfit.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Product;
import com.example.fitforfit.singleton.Database;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class AddNewIngredientManuellProduct extends AppCompatActivity {

    int allEditsFilled = 0;

    Button addButton;
    EditText prodNameEditText;
    EditText ckalEdit;
    EditText fatEdit;
    EditText satfatEdit;
    EditText carbEdit;
    EditText sugarEdit;
    EditText fiberEdit;
    EditText proteinEdit;
    EditText saltEdit;
    EditText infoEdit;

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String textFieldString = charSequence.toString().trim();

            if(prodNameEditText.getText().toString().matches("") || fatEdit.getText().toString().matches("") ||
                satfatEdit.getText().toString().matches("") || carbEdit.getText().toString().matches("") ||
                sugarEdit.getText().toString().matches("") || fiberEdit.getText().toString().matches("") ||
                proteinEdit.getText().toString().matches("") || saltEdit.getText().toString().matches("")){
                addButton.setEnabled(false);
            }else{
                addButton.setEnabled(true);
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_ingredient_manuell_product);

        initViews();
    }

    private void initViews() {

        this.addButton = findViewById(R.id.removeIngredientButton);
        Button cancelButton = findViewById(R.id.cancelIngredientButton);
        addButton.setText("Add");
        addButton.setEnabled(false);
        cancelButton.setText("Cancel");
        cancelButton.setOnClickListener(view -> {
            AppDatabase db = Database.getInstance(this);
            db.productDao().deleteProductById(db.productDao().getLastProductId());

            finish();

        });

        this.prodNameEditText = findViewById(R.id.IngredientNameText);
        this.ckalEdit = findViewById(R.id.ckalEdit);
        this.fatEdit = findViewById(R.id.fatEdit);
        this.satfatEdit = findViewById(R.id.satfatEdit);
        this.carbEdit = findViewById(R.id.carbEdit);
        this.sugarEdit = findViewById(R.id.sugarEdit);
        this.fiberEdit = findViewById(R.id.fiberEdit);
        this.proteinEdit = findViewById(R.id.proteinEdit);
        this.saltEdit = findViewById(R.id.saltEdit);
        this.infoEdit = findViewById(R.id.infoEdit);

        this.prodNameEditText.addTextChangedListener(textWatcher);
        this.ckalEdit.addTextChangedListener(textWatcher);
        this.fatEdit.addTextChangedListener(textWatcher);
        this.satfatEdit.addTextChangedListener(textWatcher);
        this.carbEdit.addTextChangedListener(textWatcher);
        this.sugarEdit.addTextChangedListener(textWatcher);
        this.fiberEdit.addTextChangedListener(textWatcher);
        this.proteinEdit.addTextChangedListener(textWatcher);
        this.saltEdit.addTextChangedListener(textWatcher);



        addButton.setOnClickListener(view -> {
            AppDatabase db = Database.getInstance(this);
            int productId = db.productDao().getLastProductId();

            Product newprod = new Product();
            newprod.product_name = prodNameEditText.getText().toString();
            newprod.ckal = Integer.parseInt(ckalEdit.getText().toString());
            newprod.fat = Float.parseFloat(fatEdit.getText().toString());
            newprod.saturated_fat = Float.parseFloat(satfatEdit.getText().toString());
            newprod.carb = Float.parseFloat(carbEdit.getText().toString());
            newprod.sugar = Float.parseFloat(sugarEdit.getText().toString());
            newprod.fiber = Float.parseFloat(fiberEdit.getText().toString());
            newprod.protein = Float.parseFloat(proteinEdit.getText().toString());
            newprod.salt = Float.parseFloat(saltEdit.getText().toString());
            newprod.info = infoEdit.getText().toString();

            db.productDao().updateProductByMealId( newprod.product_name, newprod.ckal, newprod.fat, newprod.saturated_fat, newprod.carb, newprod.sugar, newprod.fiber,newprod.protein, newprod.salt,newprod.info, productId);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("lastProdId", String.valueOf(productId));
            setResult(RESULT_OK, resultIntent);
            finish();
            //update der werte der Edittexte in product
            //productId als resultintent zurück an addingredienttomeal geben
        });
    }
}