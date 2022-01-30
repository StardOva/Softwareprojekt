package com.example.fitforfit.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.example.fitforfit.adapter.WorkoutDetailAdapter;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.databinding.FragmentMainBinding;
import com.example.fitforfit.databinding.FragmentWorkoutDetailBinding;
import com.example.fitforfit.entity.Exercise;
import com.example.fitforfit.singleton.Database;
import com.example.fitforfit.ui.main.AddExerciseToWorkoutActivity;

import java.util.List;

public class WorkoutDetailFragment extends Fragment {

    public FragmentWorkoutDetailBinding binding;
    private WorkoutDetailAdapter workoutDetailAdapter;
    public int workoutId;

    public WorkoutDetailFragment() {
        super(R.layout.fragment_workout_detail);
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
        this.binding = FragmentWorkoutDetailBinding.inflate(inflater, container, false);

        return inflater.inflate(R.layout.fragment_workout_detail, binding.getRoot());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initRecyclerView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadExerciseList();
        if (this.workoutDetailAdapter != null && this.binding != null) {
            TextView textView = this.binding.workoutDetailTextView;

            if (this.workoutDetailAdapter.getItemCount() == 0) {
                textView.setText(R.string.workout_detail_no_exercises);
            }
            else {
                textView.setVisibility(View.GONE);
            }
        }
    }

    private void initRecyclerView(View view) {
        AsyncTask.execute(() -> {
            RecyclerView recyclerView = view.findViewById(R.id.recyclerViewWorkoutExercises);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            this.workoutDetailAdapter = new WorkoutDetailAdapter(getContext());
            recyclerView.setAdapter(this.workoutDetailAdapter);
            loadExerciseList();
            initViews(view);
        });
    }

    private void loadExerciseList() {
        AppDatabase    db           = Database.getInstance(getContext());
        List<Exercise> exerciseList = db.workoutDao().getRelatedExercises(this.workoutId);
        if (exerciseList != null) {
            this.workoutDetailAdapter.setExerciseList(exerciseList);
        }
    }

    private void initViews(View view) {
        if (this.workoutDetailAdapter.getItemCount() == 0) {
            TextView textView = view.findViewById(R.id.workoutDetailTextView);
            textView.setText(R.string.workout_detail_no_exercises);
        }

        Button addExerciseBtn = view.findViewById(R.id.btnAddExercise);
        addExerciseBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), AddExerciseToWorkoutActivity.class);
            intent.putExtra("workoutId", this.workoutId);
            requireActivity().startActivity(intent);
        });
    }
}
