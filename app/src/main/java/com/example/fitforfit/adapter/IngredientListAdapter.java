package com.example.fitforfit.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.entity.Ingredient;
import com.example.fitforfit.ui.main.IngredientActivity;

import java.util.List;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.MyViewHolder>{

    private Context context;
    private Context parentActivity;

    private List<Ingredient> ingredientList;

    public IngredientListAdapter(Context context) {
        this.context = context;
    }

    public void setContext(Context parentActivity){
        this.parentActivity = parentActivity;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public IngredientListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_row_tracker_ingredient, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientListAdapter.MyViewHolder holder, int position) {
        /*String meal_name = this.mealList.get(position).meal_name;
        holder.mealButton.setText(meal_name);
        String date = this.mealList.get(position).date;
        String[] split = this.mealList.get(position).date.split("-");
        String displayDate = split[2]+"."+split[1]+".";
        holder.dayButton.setText(displayDate);

       */
        String ing_name = this.ingredientList.get(position).ingredient_name;
        holder.ingredientButton.setText(ing_name.toString());
        holder.ingredientButton.setOnClickListener(v -> {
                //öffne Ingrdeint bearbeiten activity
            Intent intent = new Intent(parentActivity, IngredientActivity.class);
            intent.putExtra("ingId", String.valueOf(ingredientList.get(position).id));
            parentActivity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return  this.ingredientList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{
        /*TextView tvFirstName;
        TextView tvLastName;*/
        /*Button mealButton;
INHALT RECYCLER ROW LYOUT*/
        Button ingredientButton;
        public MyViewHolder(View view) {
            super(view);
            /*tvFirstName = view.findViewById(R.id.tvFirstName);
            tvLastName = view.findViewById(R.id.tvLastName);
            mealButton = view.findViewById(R.id.buttonMeal);*/
            ingredientButton = view.findViewById(R.id.buttonProduct);

        }
    }
}
