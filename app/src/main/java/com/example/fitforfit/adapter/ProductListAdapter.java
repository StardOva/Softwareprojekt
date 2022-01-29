package com.example.fitforfit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.entity.Ingredient;
import com.example.fitforfit.entity.Product;
import com.example.fitforfit.ui.main.AddNewIngredientManuellProduct;
import com.example.fitforfit.ui.main.IngredientActivity;
import com.example.fitforfit.ui.main.MealActivity;
import com.example.fitforfit.ui.main.ShowProductActivity;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder>{

    private Context context;
    private Context parentActivity;

    private List<Product> productList;

    public ProductListAdapter(Context context) {
        this.context = context;
    }

    public void setContext(Context parentActivity){
        this.parentActivity = parentActivity;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ProductListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_row_tracker_product, parent, false);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.MyViewHolder holder, int position) {

        String prodName = this.productList.get(position).product_name;
        holder.productButton.setText(prodName);
        holder.productButton.setOnClickListener(view -> {
            Log.d("PRODUCT_ID",String.valueOf(productList.get(position).id));
            //start show product activity mit intent product id

            Intent intent = new Intent(parentActivity, ShowProductActivity.class);
            intent.putExtra("prodId", String.valueOf(productList.get(position).id));

            parentActivity.startActivity(intent);


        });

    }

    @Override
    public int getItemCount() {
        return  this.productList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{
        /*TextView tvFirstName;
        TextView tvLastName;*/
        /*Button mealButton;
INHALT RECYCLER ROW LYOUT*/
        Button productButton;
        public MyViewHolder(View view) {
            super(view);
            /*tvFirstName = view.findViewById(R.id.tvFirstName);
            tvLastName = view.findViewById(R.id.tvLastName);
            mealButton = view.findViewById(R.id.buttonMeal);*/
            productButton = view.findViewById(R.id.buttonProduct);

        }
    }
}
