package com.example.fitforfit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Exercise;
import com.example.fitforfit.entity.WorkoutExercise;
import com.example.fitforfit.singleton.Database;
import com.example.fitforfit.ui.main.AddExerciseToWorkoutActivity;
import com.example.fitforfit.utils.ColorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder> {

    private final Context context;
    private Context workoutDetailActivity;
    private List<Exercise> exerciseList = null;
    private final AddExerciseToWorkoutActivity parentActivity;
    ColorUtils colorUtils;

    public ArrayList<Exercise> selectedExercises = new ArrayList<>();

    public ExerciseListAdapter(Context context, AddExerciseToWorkoutActivity parentActivity) {
        this.context = context;
        this.parentActivity = parentActivity;
        this.colorUtils = new ColorUtils(context);
    }

    public void setContext(Context workoutDetailActivity) {
        this.workoutDetailActivity = workoutDetailActivity;
    }

    public void setExerciseList(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.recycler_row_exercises, parent, false);

        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(holder.getAbsoluteAdapterPosition());

        holder.btnAddExerciseToWorkout.setText(exercise.name);
        holder.btnAddExerciseToWorkout.setOnClickListener(view -> {
            // noch nicht ausgewählt -> auswählen
            if (!selectedExercises.contains(exercise)) {
                selectedExercises.add(exercise);
                holder.btnAddExerciseToWorkout.setBackgroundColor(colorUtils.getColor(R.color.fit_light_grey));
            } else {
                selectedExercises.remove(exercise);
                holder.btnAddExerciseToWorkout.setBackgroundColor(Color.TRANSPARENT);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (exerciseList != null) {
            return this.exerciseList.size();
        }

        return 0;
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {

        TextView btnAddExerciseToWorkout;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            btnAddExerciseToWorkout = itemView.findViewById(R.id.btnAddExerciseToWorkout);
        }
    }
}
