package com.example.fitforfit.adapter;

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
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Day;
import com.example.fitforfit.entity.Meal;
import com.example.fitforfit.singleton.Database;
import com.example.fitforfit.ui.main.MealActivity;
import com.example.fitforfit.ui.main.TrackerDayActivity;

import java.util.List;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.MyViewHolder>{

    private Context context;
    private Context dayActivity;

    private List<Meal> mealList = null;

    public MealListAdapter(Context context) {
        this.context = context;
    }

    public void setContext(Context dayActivity){
        Log.d("DEBUG NULLPOINTER", "CHECKPOINT 2");
        this.dayActivity = dayActivity;
    }

    public void setMealList(List<Meal> mealList) {
        this.mealList = mealList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MealListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("DEBUG NULLPOINTER", "CHECKPOINT 3");
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_row_tracker_meals, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealListAdapter.MyViewHolder holder, int position) {
        String meal_name = this.mealList.get(position).meal_name;

        holder.mealButton.setText(meal_name);
       /* String date = this.mealList.get(position).date;
        String[] split = this.mealList.get(position).date.split("-");
        String displayDate = split[2]+"."+split[1]+".";
        holder.dayButton.setText(displayDate);
*/
            holder.mealButton.setOnClickListener(v -> {

                Intent intent = new Intent(dayActivity, MealActivity.class);
                intent.putExtra("mealId", String.valueOf(mealList.get(position).id));
                dayActivity.startActivity(intent);

            });


    }

    @Override
    public int getItemCount() {
        Log.d("DEBUG NULLPOINTER", "CHECKPOINT 4");
        //return  this.mealList.size();
        if (this.mealList != null) {
            return this.mealList.size();
        }

        return 0;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{
        /*TextView tvFirstName;
        TextView tvLastName;*/
        Button mealButton;

        public MyViewHolder(View view) {
            super(view);
            /*tvFirstName = view.findViewById(R.id.tvFirstName);
            tvLastName = view.findViewById(R.id.tvLastName);*/
            mealButton = view.findViewById(R.id.buttonMeal);

        }
    }
}
