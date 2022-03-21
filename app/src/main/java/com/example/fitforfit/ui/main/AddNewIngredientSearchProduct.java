package com.example.fitforfit.ui.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.adapter.ProductListAdapter;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Product;
import com.example.fitforfit.singleton.Database;

import java.util.List;


public class AddNewIngredientSearchProduct extends BaseActivity {

    ProductListAdapter productListAdapter;
    List<Product> productList;
    EditText ingredientNameTextView;
    int mealId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_ingredient_search_product);

        initToolbar();

        initViews();
        initRecyclerView();// zeigt alle Zutaten des meals


    }



    private void loadProductList(String search) {
        AppDatabase db = Database.getInstance(this);
        if(search.equals("NO_SEARCH_INPUT")){
            productList = db.productDao().getAllProducts();
        }else{
            productList = db.productDao().searchProductsByName(search);
        }


        this.runOnUiThread(() -> {
            productListAdapter.setContext(this);
            productListAdapter.setProductList(productList);
        });
    }

    private void initRecyclerView() {

            RecyclerView recyclerViewMeals = findViewById(R.id.recyclerViewProduct);
            recyclerViewMeals.setLayoutManager(new LinearLayoutManager(this));

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            recyclerViewMeals.addItemDecoration(dividerItemDecoration);
            productListAdapter = new ProductListAdapter(this, this.mealId);
            recyclerViewMeals.setAdapter(productListAdapter);
            loadProductList("NO_SEARCH_INPUT");

            //AsyncTask.execute(this::loadProductList("NO_SEARCH_INPUT"));


    }

    private void initViews() {
        if(getIntent().hasExtra("mealId")) {
            String mealIdS = getIntent().getStringExtra("mealId");
            this.mealId = Integer.valueOf(mealIdS);
            Log.d("GET_MEAL_ID", String.valueOf(this.mealId));
        }

        this.ingredientNameTextView = findViewById(R.id.IngredientNameTextView);
        ingredientNameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String textFieldString = charSequence.toString().trim();
                String ingName = ingredientNameTextView.getText().toString();
                loadProductList(ingName);


            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
    }
}