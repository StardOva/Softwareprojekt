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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_ingredient_manuell_product);

        initViews();
    }

    private void initViews() {

        Button addButton = findViewById(R.id.addProductManuell);
        Button cancelButton = findViewById(R.id.cancelProdcutManuell);
        addButton.setText("Add");
        cancelButton.setText("Cancel");
        cancelButton.setOnClickListener(view -> {
            AppDatabase db = Database.getInstance(this);
            db.productDao().deleteProductById(db.productDao().getLastProductId());

            finish();

        });

        EditText prodNameEditText = findViewById(R.id.productNameEdit);
        EditText proteinEdit = findViewById(R.id.proteinEdit);
        EditText infoEdit = findViewById(R.id.infoEdit);
        addButton.setEnabled(false);
        prodNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String textFieldString = charSequence.toString().trim();
                // bei Länge 0
                if (textFieldString.length() == 0) {
                    addButton.setEnabled(false);
                    allEditsFilled--;
                    return;
                }else{
                    allEditsFilled++;
                }
                // ansonsten den Button aktivieren
                if(allEditsFilled > 2) {
                    addButton.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        proteinEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String textFieldString = charSequence.toString().trim();
                // bei Länge 0
                if (textFieldString.length() == 0) {
                    addButton.setEnabled(false);
                    allEditsFilled--;
                    return;
                }else{
                    allEditsFilled++;
                }
                // ansonsten den Button aktivieren
                if(allEditsFilled > 2) {
                    addButton.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        infoEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String textFieldString = charSequence.toString().trim();
                // bei Länge 0
                if (textFieldString.length() == 0) {
                    addButton.setEnabled(false);
                    allEditsFilled--;
                    return;
                }else{
                    allEditsFilled++;
                }
                // ansonsten den Button aktivieren
                if(allEditsFilled > 2) {
                    addButton.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addButton.setOnClickListener(view -> {
            AppDatabase db = Database.getInstance(this);
            int productId = db.productDao().getLastProductId();

            Product newprod = new Product();
            newprod.product_name = prodNameEditText.getText().toString();
            newprod.protein = Float.parseFloat(proteinEdit.getText().toString());
            newprod.info = infoEdit.getText().toString();

            db.productDao().updateProductByMealId( newprod.product_name, newprod.protein, newprod.info, productId);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("lastProdId", String.valueOf(productId));
            setResult(RESULT_OK, resultIntent);
            finish();
            //update der werte der Edittexte in product
            //productId als resultintent zurück an addingredienttomeal geben
        });
    }
}