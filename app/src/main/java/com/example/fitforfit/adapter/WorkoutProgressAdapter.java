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
import com.example.fitforfit.ui.main.TrainingActivity;

import java.util.ArrayList;

public class WorkoutProgressAdapter extends RecyclerView.Adapter<WorkoutProgressAdapter.WorkoutProgressViewHolder> {

    private Context parentActivity;
    private final Context context;
    private final int workoutId;

    // Liste aller Timestamps
    private ArrayList<String> workoutProgressList = new ArrayList<>();

    private int[] trainingIdArray;

    public WorkoutProgressAdapter(Context context, int workoutId) {
        this.context = context;
        this.workoutId = workoutId;
    }

    public void setParentActivity(Context context) {
        this.parentActivity = context;
    }

    public void setWorkoutProgressList(ArrayList<String> workoutProgressList, int[] trainingIdArray) {
        this.workoutProgressList = workoutProgressList;
        this.trainingIdArray = trainingIdArray;
    }

    @NonNull
    @Override
    public WorkoutProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.recycler_row_workout_progress, parent, false);

        return new WorkoutProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutProgressViewHolder holder, int position) {
        // setze den String createdAt der Trainingssession als ButtonText
        String createdAt = workoutProgressList.get(position);
        holder.workoutProgressButton.setText(createdAt);
        holder.workoutProgressButton.setOnClickListener(view -> {
            Intent intent = new Intent(this.parentActivity, TrainingActivity.class);
            intent.putExtra("workoutId", this.workoutId);
            intent.putExtra("trainingId", this.trainingIdArray[position]);
            intent.putExtra("createdAt", createdAt);
            this.parentActivity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return workoutProgressList.size();
    }

    public static class WorkoutProgressViewHolder extends RecyclerView.ViewHolder {

        Button workoutProgressButton;

        public WorkoutProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutProgressButton = itemView.findViewById(R.id.workout_progress_recycler_btn);
        }
    }
}
