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
import com.example.fitforfit.entity.Workout;
import com.example.fitforfit.ui.main.WorkoutDetailActivity;

import java.util.List;

public class WorkoutListAdapter extends RecyclerView.Adapter<WorkoutListAdapter.WorkoutViewHolder> {

    private Context context;
    private Context mainActivity;
    private List<Workout> workoutList;

    public WorkoutListAdapter(Context context){
        this.context = context;
    }

    public void setContext(Context mainActivity){
        this.mainActivity = mainActivity;
    }

    public void setWorkoutList(List<Workout> workoutList){
        this.workoutList = workoutList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WorkoutListAdapter.WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.recycler_row_workouts, parent, false);

        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutListAdapter.WorkoutViewHolder holder, int position) {
        String name = this.workoutList.get(position).name;
        holder.workoutButton.setText(name);
        holder.workoutButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), WorkoutDetailActivity.class);
            intent.putExtra("workoutId", this.workoutList.get(position).id);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return this.workoutList.size();
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {

        Button workoutButton;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutButton = itemView.findViewById(R.id.buttonWorkout);
        }
    }

}


