package com.example.fitforfit.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.adapter.ExerciseListAdapter;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.databinding.FragmentAddExerciseToWorkoutBinding;
import com.example.fitforfit.entity.Exercise;
import com.example.fitforfit.singleton.Database;
import com.example.fitforfit.ui.main.AddExerciseToWorkoutActivity;
import com.example.fitforfit.ui.main.CreateNewExerciseActivity;

import java.util.List;

public class AddExerciseToWorkoutFragment extends BaseFragment {

    public FragmentAddExerciseToWorkoutBinding binding = null;
    public ExerciseListAdapter exerciseListAdapter = null;
    public int workoutId;

    private AddExerciseToWorkoutActivity parentActivity;

    public AddExerciseToWorkoutFragment() {
        super(R.layout.fragment_add_exercise_to_workout);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            this.workoutId = args.getInt("workoutId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentAddExerciseToWorkoutBinding.inflate(inflater, container, false);

        return inflater.inflate(R.layout.fragment_add_exercise_to_workout, this.binding.getRoot());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initRecyclerView(view);

        initToolbar();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadExerciseList();
        if (this.exerciseListAdapter != null && this.binding != null) {
            TextView textView = requireView().findViewById(R.id.addExerciseToWorkoutTextView);

            if (this.exerciseListAdapter.getItemCount() == 0) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(R.string.add_exercise_to_workout_no_exercises);
            } else {
                textView.setVisibility(View.GONE);
            }
        }
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewExercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        this.exerciseListAdapter = new ExerciseListAdapter(getContext(), this.parentActivity);
        this.exerciseListAdapter.setWorkoutId(this.workoutId);
        recyclerView.setAdapter(this.exerciseListAdapter);

        initViews(view);
        loadExerciseList();
    }

    private void loadExerciseList() {
        AsyncTask.execute(() -> {
            AppDatabase db = Database.getInstance(getContext());
            List<Exercise> exerciseList = db.exerciseDao().getUnusedExercisesForWorkout(this.workoutId);

            // bereits hinzugefügte auch einladen
            List<Exercise> usedExerciseList = db.exerciseDao().getUsedExercisesForWorkout(this.workoutId);

            if (exerciseList != null) {
                requireActivity().runOnUiThread(() -> {
                    this.exerciseListAdapter.setExerciseList(exerciseList);
                    TextView textView = requireView().findViewById(R.id.addExerciseToWorkoutTextView);

                    if (this.exerciseListAdapter.getItemCount() == 0) {
                        textView.setText(R.string.add_exercise_to_workout_no_exercises);
                    } else {
                        textView.setVisibility(View.GONE);

                    }
                });
            }

            if (usedExerciseList != null) {
                requireActivity().runOnUiThread(() -> {
                    TextView usedTextView = requireView().findViewById(R.id.textViewAddedExercises);
                    StringBuilder text = new StringBuilder("Folgende Übungen wurden bereits hinzugefügt:\n");
                    for (Exercise exercise : usedExerciseList) {
                        text.append("- ").append(exercise.name).append("\n");
                    }

                    usedTextView.setText(text.toString());
                });
            }

        });
    }

    private void initViews(View view) {
        Button btnCreateNewExercise = view.findViewById(R.id.btnCreateNewExercise);
        btnCreateNewExercise.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireActivity(), CreateNewExerciseActivity.class);
            requireActivity().startActivity(intent);
        });
    }

    public void setParentActivity(AddExerciseToWorkoutActivity parentActivity) {
        this.parentActivity = parentActivity;
    }
}
