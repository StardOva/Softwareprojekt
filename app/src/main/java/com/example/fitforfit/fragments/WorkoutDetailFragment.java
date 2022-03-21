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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.adapter.WorkoutDetailAdapter;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.databinding.FragmentWorkoutDetailBinding;
import com.example.fitforfit.dragndrop.WorkoutDetailMoveCallback;
import com.example.fitforfit.entity.Exercise;
import com.example.fitforfit.entity.Training;
import com.example.fitforfit.singleton.Database;
import com.example.fitforfit.ui.main.AddExerciseToWorkoutActivity;
import com.example.fitforfit.ui.main.SettingsActivity;
import com.example.fitforfit.ui.main.TrainingActivity;
import com.example.fitforfit.ui.main.WorkoutStatsActivity;
import com.example.fitforfit.utils.DateUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WorkoutDetailFragment extends BaseFragment {

    public FragmentWorkoutDetailBinding binding;
    private WorkoutDetailAdapter workoutDetailAdapter = null;
    public int workoutId = 0;
    private FloatingActionButton fab = null;

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

        //return inflater.inflate(R.layout.fragment_workout_detail, this.binding.getRoot());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initRecyclerView(view);

        initWorkoutDetailToolbar(workoutId);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadExerciseList();
        if (this.workoutDetailAdapter != null && this.binding != null) {
            TextView textView = this.binding.workoutDetailTextView;

            if (this.workoutDetailAdapter.getItemCount() == 0) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(R.string.workout_detail_no_exercises);
            } else {
                textView.setVisibility(View.GONE);
                if (fab != null) {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        }

        initFab();
    }

    private void initRecyclerView(View view) {
        initViews();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewWorkoutExercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        this.workoutDetailAdapter = new WorkoutDetailAdapter(getContext(), this.workoutId, this);

        ItemTouchHelper.Callback callback = new WorkoutDetailMoveCallback(this.workoutDetailAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(this.workoutDetailAdapter);

        loadExerciseList();
    }

    private void loadExerciseList() {
        AsyncTask.execute(() -> {
            AppDatabase db = Database.getInstance(getContext());
            List<Exercise> exerciseList = db.workoutDao().getRelatedExercises(this.workoutId);
            if (exerciseList != null) {
                requireActivity().runOnUiThread(() -> {
                    this.workoutDetailAdapter.setExerciseList(exerciseList);

                    TextView textView = requireView().findViewById(R.id.workoutDetailTextView);
                    if (this.workoutDetailAdapter.getItemCount() == 0) {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText(R.string.workout_detail_no_exercises);
                    }
                    else {
                        textView.setVisibility(View.GONE);
                        if (fab != null) {
                            fab.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }

    private void initViews() {
        Button addExerciseBtn = requireView().findViewById(R.id.btnAddExercise);
        addExerciseBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), AddExerciseToWorkoutActivity.class);
            intent.putExtra("workoutId", workoutId);
            requireActivity().startActivity(intent);
        });

        initFab();
    }

    private void initFab() {
        AsyncTask.execute(() -> {
            AppDatabase db = Database.getInstance(getContext());

            LocalDate date = LocalDate.now();
            String dateString = date.format(DateTimeFormatter.ofPattern(DateUtils.getGermanDateFormat()));
            Training training = db.trainingDao().getTrainingByDate(dateString);

            requireActivity().runOnUiThread(() -> {
                fab = requireView().findViewById(R.id.fabStartTraining);

                Intent intent = new Intent(getActivity(), TrainingActivity.class);
                intent.putExtra("workoutId", workoutId);

                if (training != null) {
                    intent.putExtra("trainingId", training.id);
                    intent.putExtra("createdAt", training.createdAt);
                    intent.putExtra("continueTrainingToday", 1);
                } else {
                    intent.putExtra("trainingId", 0);
                    intent.putExtra("createdAt", dateString);
                }

                fab.setOnClickListener(view1 -> requireActivity().startActivity(intent));
            });
        });
    }

    public FloatingActionButton getFab() {
        return this.fab;
    }
}
