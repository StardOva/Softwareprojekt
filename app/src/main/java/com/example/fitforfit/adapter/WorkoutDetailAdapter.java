package com.example.fitforfit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.dragndrop.WorkoutDetailMoveCallback;
import com.example.fitforfit.entity.Exercise;
import com.example.fitforfit.entity.WorkoutExercise;
import com.example.fitforfit.fragments.WorkoutDetailFragment;
import com.example.fitforfit.singleton.Database;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

public class WorkoutDetailAdapter extends RecyclerView.Adapter<WorkoutDetailAdapter.WorkoutDetailViewHolder> implements WorkoutDetailMoveCallback.ItemTouchHelperContract {

    private final Context context;
    private List<Exercise> exerciseList = null;
    private final int workoutId;
    private final AppDatabase db;
    private final WorkoutDetailFragment workoutDetailFragment;

    public WorkoutDetailAdapter(Context context, int workoutId, WorkoutDetailFragment workoutDetailFragment) {
        this.context = context;
        this.workoutId = workoutId;
        this.workoutDetailFragment = workoutDetailFragment;
        db = Database.getInstance(context);
    }

    public void setExerciseList(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
        notifyDataSetChanged();

        FloatingActionButton fab = this.workoutDetailFragment.requireView().findViewById(R.id.fabStartTraining);

        if (this.exerciseList.size() == 0) {
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public WorkoutDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.recycler_row_workout_exercises, parent, false);

        return new WorkoutDetailViewHolder(view);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onBindViewHolder(@NonNull WorkoutDetailViewHolder holder, int position) {
        if (this.exerciseList != null) {
            String name = this.exerciseList.get(position).name;
            holder.exerciseName.setText(name);
        }

        holder.optionsMenu.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(this.context, holder.optionsMenu);
            popup.inflate(R.menu.exercise_options_menu);
            popup.setOnMenuItemClickListener(menuItem -> {
                int pos = holder.getAbsoluteAdapterPosition();

                switch (menuItem.getItemId()) {
                    case R.id.renameExercise:
                        // TODO rename exercise
                        return true;
                    case R.id.deleteAssignment:
                        new AlertDialog.Builder(context)
                                .setTitle("Bestätigen")
                                .setMessage("Soll die Übung aus dem Workout entfernt werden? Dabei wird auch die Statistik zu dieser Übung gelöscht!")
                                .setPositiveButton("JA", (dialogInterface, i) -> {
                                    AsyncTask.execute(() -> removeExerciseFromWorkout(pos));
                                })
                                .setNegativeButton("NEIN", null)
                                .show();
                        return true;
                    case R.id.deleteExercise:
                        new AlertDialog.Builder(context)
                                .setTitle("Bestätigen")
                                .setMessage("Soll die Übung gelöscht werden? Dabei wird auch die Statistik zu dieser Übung gelöscht!")
                                .setPositiveButton("JA", (dialogInterface, i) -> {
                                    AsyncTask.execute(() -> {
                                        Exercise exercise = exerciseList.get(pos);
                                        removeExerciseFromWorkout(pos);
                                        // Übung selbst löschen
                                        db.exerciseDao().delete(exercise);
                                    });
                                })
                                .setNegativeButton("NEIN", null)
                                .show();
                        return true;
                    default:
                        return false;
                }
            });
            popup.show();
        });
    }

    private void removeExerciseFromWorkout(int pos) {
        // alle Statistiken zu dieser Übung aus der Datenbank löschen
        db.trainingDao().deleteByWorkoutAndExerciseId(workoutId, exerciseList.get(pos).id);

        // Verknüpfung zum Workout löschen
        db.workoutExerciseDao().deleteByWorkoutAndExerciseId(workoutId, exerciseList.get(pos).id);
        // aus der Liste löschen
        exerciseList.remove(pos);

        workoutDetailFragment.requireActivity().runOnUiThread(() -> {
            notifyItemRemoved(pos);
            // Training kann nicht gestartet werden, wenn es keine Übungen gibt
            if (exerciseList.size() == 0){
                workoutDetailFragment.getFab().setVisibility(View.GONE);
            }
        });

        int i = 1;
        // Liste mit neuer Position in der Datenbank speichern
        for (Exercise exercise : exerciseList) {
            WorkoutExercise workoutExercise = new WorkoutExercise();
            workoutExercise.exerciseId = exercise.id;
            workoutExercise.workoutId = workoutId;
            workoutExercise.pos = i;
            db.workoutExerciseDao().update(workoutExercise);
            i++;
        }
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
            int         i  = 1;
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
        TextView optionsMenu;

        public WorkoutDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exerciseName);
            optionsMenu = itemView.findViewById(R.id.textViewOptions);
        }

    }
}
