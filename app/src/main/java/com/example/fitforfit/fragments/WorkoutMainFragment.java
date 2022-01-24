package com.example.fitforfit.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitforfit.R;
import com.example.fitforfit.dao.WorkoutDao;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.databinding.FragmentMainBinding;
import com.example.fitforfit.entity.Workout;
import com.example.fitforfit.singleton.Database;
import com.example.fitforfit.ui.main.CreateNewWorkoutActivity;

public class WorkoutMainFragment extends Fragment {

    private FragmentMainBinding binding;

    public WorkoutMainFragment(){
        super(R.layout.fragment_workout);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);

        return inflater.inflate(R.layout.fragment_workout, binding.getRoot());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button createNewWorkout = view.findViewById(R.id.btnNewWorkout);
        createNewWorkout.setOnClickListener(view1 -> {
            /*
            AsyncTask.execute(() -> {
                AppDatabase db = Database.getInstance(getContext());

                String workoutName = "Das Workout";

                Workout workout = new Workout();
                workout.name = workoutName;
                db.workoutDao().insert(workout);
            });
            */

            Intent intent = new Intent(view.getContext(), CreateNewWorkoutActivity.class);
            view.getContext().startActivity(intent);

        });
    }
}
