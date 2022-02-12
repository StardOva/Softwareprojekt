package com.example.fitforfit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Workout;
import com.example.fitforfit.fragments.WorkoutMainFragment;
import com.example.fitforfit.singleton.Database;
import com.example.fitforfit.ui.main.CreateNewWorkoutActivity;
import com.example.fitforfit.ui.main.WorkoutDetailActivity;

import java.util.List;

public class WorkoutListAdapter extends RecyclerView.Adapter<WorkoutListAdapter.WorkoutViewHolder> {

    private final Context context;
    private Context mainActivity;
    private List<Workout> workoutList = null;
    private final WorkoutMainFragment workoutMainFragment;

    public WorkoutListAdapter(WorkoutMainFragment workoutMainFragment) {
        this.workoutMainFragment = workoutMainFragment;
        this.context = workoutMainFragment.requireContext();
    }

    public void setContext(Context mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setWorkoutList(List<Workout> workoutList) {
        this.workoutList = workoutList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WorkoutListAdapter.WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.recycler_row_workouts, parent, false);

        return new WorkoutViewHolder(view);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onBindViewHolder(@NonNull WorkoutListAdapter.WorkoutViewHolder holder, int position) {
        String name = this.workoutList.get(position).name;
        holder.textViewWorkout.setText(name);
        holder.textViewWorkout.setOnClickListener(view -> {
            Intent intent = new Intent(this.mainActivity, WorkoutDetailActivity.class);
            intent.putExtra("workoutId", this.workoutList.get(position).id);
            this.mainActivity.startActivity(intent);
        });

        holder.optionsMenu.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(this.context, holder.optionsMenu);
            popup.inflate(R.menu.workout_options_menu);
            popup.setOnMenuItemClickListener(menuItem -> {
                int pos = holder.getAbsoluteAdapterPosition();

                switch (menuItem.getItemId()) {
                    case R.id.renameWorkout:
                        Intent intent = new Intent(workoutMainFragment.requireActivity(), CreateNewWorkoutActivity.class);
                        intent.putExtra("workoutId", workoutList.get(pos).id);
                        workoutMainFragment.startActivity(intent);
                        return true;
                    case R.id.deleteWorkout:
                        new AlertDialog.Builder(context)
                                .setTitle("Bestätigen")
                                .setMessage("Soll das Workout gelöscht werden?")
                                .setPositiveButton("JA", (dialogInterface, i) -> {
                                    AsyncTask.execute(() -> {
                                        AppDatabase db = Database.getInstance(context);
                                        // aus der Datenbank löschen
                                        // Einträge in anderen Tabellen werden durch das onDelete = CASCADE automatisch gelöscht
                                        db.workoutDao().delete(workoutList.get(pos));

                                        // aus der Liste löschen
                                        workoutList.remove(pos);

                                        // UI updaten
                                        workoutMainFragment.requireActivity().runOnUiThread(() -> {
                                            notifyItemRemoved(pos);
                                        });
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

    @Override
    public int getItemCount() {
        if (this.workoutList != null) {
            return this.workoutList.size();
        }

        return 0;
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {

        TextView textViewWorkout;
        TextView optionsMenu;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWorkout = itemView.findViewById(R.id.textViewWorkout);
            optionsMenu = itemView.findViewById(R.id.workoutTextViewOptions);
        }
    }

}


