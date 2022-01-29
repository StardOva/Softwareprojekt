package com.example.fitforfit.fragments;

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

import java.util.List;

public class AddExerciseToWorkoutFragment extends Fragment {

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
    }

    @Override
    public void onResume() {
        super.onResume();
        loadExerciseList();
    }

    private void initRecyclerView(View view) {
        AsyncTask.execute(() -> {
            RecyclerView recyclerView = view.findViewById(R.id.recyclerViewExercises);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            this.exerciseListAdapter = new ExerciseListAdapter(getContext(), this.parentActivity);
            this.exerciseListAdapter.setWorkoutId(this.workoutId);
            recyclerView.setAdapter(this.exerciseListAdapter);
            loadExerciseList();
            initViews(view);
        });
    }

    private void loadExerciseList() {
        AppDatabase    db           = Database.getInstance(getContext());
        List<Exercise> exerciseList = db.exerciseDao().getAll();
        if (exerciseList != null) {
            this.exerciseListAdapter.setExerciseList(exerciseList);
        }
    }

    private void initViews(View view) {
        if (this.exerciseListAdapter.getItemCount() == 0) {
            TextView textView = view.findViewById(R.id.addExerciseToWorkoutTextView);
            textView.setText(R.string.add_exercise_to_workout_no_exercises);
        }

        Button btnCreateNewExercise = view.findViewById(R.id.btnCreateNewExercise);
        btnCreateNewExercise.setOnClickListener(view1 -> {
            // TODO neue Activity wo man eine Übung anlegt
        });
    }

    public void setParentActivity(AddExerciseToWorkoutActivity parentActivity) {
        this.parentActivity = parentActivity;
    }
}
