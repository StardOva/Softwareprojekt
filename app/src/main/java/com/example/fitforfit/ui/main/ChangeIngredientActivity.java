package com.example.fitforfit.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Ingredient;
import com.example.fitforfit.singleton.Database;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;

public class ChangeIngredientActivity extends AppCompatActivity {

    int ingId;
    Ingredient ing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_ingredient);

        initViews();
    }

    private void initViews() {

        String ingIdS = getIntent().getStringExtra("ingId");
        this.ingId = Integer.valueOf(ingIdS);

        AppDatabase db = Database.getInstance(this);
        this.ing = db.ingredientDao().getIngredientById(this.ingId);


        EditText ingName = findViewById(R.id.ingName);
        EditText quant = findViewById(R.id.quantity);
        ingName.setText(this.ing.ingredient_name);
        quant.setText(String.valueOf(this.ing.quantity));

        Button saveBtn = findViewById(R.id.saveBtn);
        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> {finish();});

        saveBtn.setOnClickListener(view -> {
            db.ingredientDao().updateNameQuantityById(String.valueOf(ingName.getText()), Integer.parseInt(String.valueOf(quant.getText())), this.ingId);
            finish();
        });
    }
}