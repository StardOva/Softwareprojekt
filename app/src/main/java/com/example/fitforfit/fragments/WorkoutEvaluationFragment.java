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
import com.example.fitforfit.entity.Training;
import com.example.fitforfit.singleton.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkoutEvaluationFragment extends BaseFragment {

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

    @Override
    public void onResume() {
        super.onResume();
        loadExerciseList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentMainBinding.inflate(inflater, container, false);

        return inflater.inflate(R.layout.fragment_workout_evaluation, this.binding.getRoot());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            this.workoutId = getArguments().getInt("workoutId");
        }
        initRecyclerView(view);

        initToolbar(getString(R.string.workout_name));
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewWorkoutEvaluation);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        this.adapter = new WorkoutEvaluationAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        loadExerciseList();
    }

    private void loadExerciseList() {
        AsyncTask.execute(() -> {
            if (adapter != null && workoutId > 0) {
                AppDatabase    db           = Database.getInstance(getContext());
                List<Exercise> exerciseList = db.workoutDao().getRelatedExercises(workoutId);

                HashMap<Integer, ArrayList<Training>> exerciseTrainingList = new HashMap<>();

                int[] trainingIds = db.trainingDao().getAllIds(workoutId);

                if (exerciseList != null && exerciseList.size() > 0 && trainingIds != null && trainingIds.length > 0) {
                    for (Exercise exercise : exerciseList) {
                        ArrayList<Training> maxSets = new ArrayList<>();

                        for (int id : trainingIds) {
                            Training training = db.trainingDao().getMaxWeightSet(id, workoutId, exercise.id);
                            maxSets.add(training);
                        }
                        exerciseTrainingList.put(exercise.id, maxSets);
                    }

                    adapter.setExerciseTrainingList(exerciseTrainingList);

                    requireActivity().runOnUiThread(() -> adapter.setExerciseList(exerciseList));
                }
            }
        });
    }

}
