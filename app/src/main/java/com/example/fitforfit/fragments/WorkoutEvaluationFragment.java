package com.example.fitforfit.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.adapter.WorkoutEvaluationAdapter;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.databinding.FragmentMainBinding;
import com.example.fitforfit.entity.Exercise;
import com.example.fitforfit.singleton.Database;

import java.util.List;

public class WorkoutEvaluationFragment extends Fragment {

    private FragmentMainBinding binding;
    private WorkoutEvaluationAdapter adapter = null;
    private int workoutId = 0;

    public WorkoutEvaluationFragment() {
        super(R.layout.fragment_workout_evaluation);
    }

    public static WorkoutEvaluationFragment newInstance(int workoutId) {
        Bundle args = new Bundle();
        args.putInt("workoutId", workoutId);
        WorkoutEvaluationFragment fragment = new WorkoutEvaluationFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentMainBinding.inflate(inflater, container, false);

        return inflater.inflate(R.layout.fragment_workout_evaluation, this.binding.getRoot());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.workoutId = getArguments().getInt("workoutId");
        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewWorkoutEvaluation);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        this.adapter = new WorkoutEvaluationAdapter(getActivity(), workoutId);
        recyclerView.setAdapter(adapter);
        loadExerciseList();
    }

    private void loadExerciseList() {
        AsyncTask.execute(() -> {
            AppDatabase    db           = Database.getInstance(getContext());
            List<Exercise> exerciseList = db.workoutDao().getRelatedExercises(workoutId);

            requireActivity().runOnUiThread(() -> adapter.setExerciseList(exerciseList));
        });
    }

}
