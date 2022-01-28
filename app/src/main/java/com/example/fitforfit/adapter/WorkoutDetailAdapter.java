package com.example.fitforfit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.entity.Exercise;

import java.util.List;

public class WorkoutDetailAdapter extends RecyclerView.Adapter<WorkoutDetailAdapter.WorkoutDetailViewHolder> {

    private Context context;
    private List<Exercise> exerciseList = null;

    public WorkoutDetailAdapter(Context context){
        this.context = context;
    }

    public void setExerciseList(List<Exercise> exerciseList){
        this.exerciseList = exerciseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WorkoutDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.recycler_row_workout_exercises, parent, false);

        return new WorkoutDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutDetailViewHolder holder, int position) {

        if (this.exerciseList != null){
            String name = this.exerciseList.get(position).name;
            holder.exerciseButton.setText(name);
        }
        // TODO Button oder Listeneintrag anders stylen?
    }

    @Override
    public int getItemCount() {
        return this.exerciseList.size();
    }

    public static class WorkoutDetailViewHolder extends RecyclerView.ViewHolder{

        Button exerciseButton;

        public WorkoutDetailViewHolder(@NonNull View itemView){
            super(itemView);
            exerciseButton = itemView.findViewById(R.id.buttonExercise);
        }

    }
}
