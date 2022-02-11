package com.example.fitforfit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.dragndrop.WorkoutDetailMoveCallback;
import com.example.fitforfit.entity.Exercise;
import com.example.fitforfit.entity.WorkoutExercise;
import com.example.fitforfit.singleton.Database;

import java.util.Collections;
import java.util.List;

public class WorkoutDetailAdapter extends RecyclerView.Adapter<WorkoutDetailAdapter.WorkoutDetailViewHolder> implements WorkoutDetailMoveCallback.ItemTouchHelperContract {

    private Context context;
    private List<Exercise> exerciseList = null;
    private int workoutId;

    public WorkoutDetailAdapter(Context context, int workoutId) {
        this.context = context;
        this.workoutId = workoutId;
    }

    public void setExerciseList(List<Exercise> exerciseList) {
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
        if (this.exerciseList != null) {
            String name = this.exerciseList.get(position).name;
            holder.exerciseName.setText(name);
        }
        // TODO Button oder Listeneintrag anders stylen?
    }

    @Override
    public int getItemCount() {
        if (this.exerciseList != null) {
            return this.exerciseList.size();
        }

        return 0;
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(this.exerciseList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(this.exerciseList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(WorkoutDetailViewHolder myViewHolder) {
        myViewHolder.exerciseName.setBackgroundColor(Color.LTGRAY);
    }

    @Override
    public void onRowClear(WorkoutDetailViewHolder myViewHolder) {
        myViewHolder.exerciseName.setBackgroundColor(Color.WHITE);
        // jetzt noch die neue Liste asynchron speichern
        AsyncTask.execute(() -> {
            int i = 1;
            AppDatabase db = Database.getInstance(this.context);

            for (Exercise exercise : exerciseList) {
                WorkoutExercise workoutExercise = new WorkoutExercise();
                workoutExercise.workoutId = workoutId;
                workoutExercise.exerciseId = exercise.id;
                workoutExercise.pos = i;
                db.workoutExerciseDao().update(workoutExercise);
                i++;
            }
        });
    }

    public static class WorkoutDetailViewHolder extends RecyclerView.ViewHolder {

        TextView exerciseName;

        public WorkoutDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exerciseName);
        }

    }
}
