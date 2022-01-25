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
import com.example.fitforfit.entity.Day;
import com.example.fitforfit.entity.Meal;
import com.example.fitforfit.ui.main.TrackerDayActivity;

import java.util.List;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.MyViewHolder>{

    private Context context;
    private Context dayActivity;

    private List<Meal> mealList;

    public MealListAdapter(Context context) {
        this.context = context;
    }

    public void setContext(Context dayActivity){
        this.dayActivity = dayActivity;
    }

    public void setDayList(List<Meal> mealList) {
        this.mealList = mealList;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public MealListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_row_tracker_meals, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealListAdapter.MyViewHolder holder, int position) {
        holder.mealButton.setText("Hi");
       /* String date = this.mealList.get(position).date;
        String[] split = this.mealList.get(position).date.split("-");
        String displayDate = split[2]+"."+split[1]+".";
        holder.dayButton.setText(displayDate);

        holder.dayButton.setOnClickListener(v -> {
            Log.d("DayButton", split[2]+"."+split[1]+".");

            Intent intent = new Intent(dayActivity, TrackerDayActivity.class);
            intent.putExtra("date", date);
            dayActivity.startActivity(intent);

        });*/
    }

    @Override
    public int getItemCount() {
        return  this.mealList.size();
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
