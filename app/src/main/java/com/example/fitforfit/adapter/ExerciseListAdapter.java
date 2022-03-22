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

import java.util.HashMap;
import java.util.List;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder> {

    private final Context context;
    private Context workoutDetailActivity;
    private List<Exercise> exerciseList = null;
    private final AddExerciseToWorkoutActivity parentActivity;
    private int workoutId;
    ColorUtils colorUtils;

    public HashMap<Integer, Exercise> selectedExercises = new HashMap<>();


    public ExerciseListAdapter(Context context, AddExerciseToWorkoutActivity parentActivity) {
        this.context = context;
        this.parentActivity = parentActivity;
        this.colorUtils = new ColorUtils(context);
    }

    public void setContext(Context workoutDetailActivity) {
        this.workoutDetailActivity = workoutDetailActivity;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
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
            if (selectedExercises.get(exercise.id) == null) {
                selectedExercises.put(exercise.id, exercise);
                holder.btnAddExerciseToWorkout.setBackgroundColor(colorUtils.getColor(R.color.fit_light_grey));
            } else {
                selectedExercises.remove(exercise.id);
                holder.btnAddExerciseToWorkout.setBackgroundColor(Color.TRANSPARENT);
            }

            // speichere die Zuordnung und generiere eine Position
            /*
            AsyncTask.execute(() -> {
                AppDatabase db = Database.getInstance(this.context);

                WorkoutExercise workoutExercise = new WorkoutExercise();
                workoutExercise.workoutId = this.workoutId;
                workoutExercise.exerciseId = this.exerciseList.get(position).id;

                int pos = db.workoutExerciseDao().getLastPosByWorkoutId(this.workoutId);
                workoutExercise.pos = pos + 1;

                db.workoutExerciseDao().insert(workoutExercise);
            });

            this.parentActivity.finish();
             */
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
