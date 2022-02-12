package com.example.fitforfit.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.adapter.WorkoutListAdapter;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.databinding.FragmentMainBinding;
import com.example.fitforfit.databinding.FragmentWorkoutBinding;
import com.example.fitforfit.entity.Workout;
import com.example.fitforfit.singleton.Database;
import com.example.fitforfit.ui.main.CreateNewWorkoutActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class WorkoutMainFragment extends Fragment {

    private FragmentMainBinding binding;
    private WorkoutListAdapter workoutListAdapter;

    public WorkoutMainFragment() {
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
            Intent intent = new Intent(view.getContext(), CreateNewWorkoutActivity.class);
            view.getContext().startActivity(intent);
        });

        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewWorkouts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        this.workoutListAdapter = new WorkoutListAdapter(this);
        recyclerView.setAdapter(this.workoutListAdapter);
        loadWorkoutList();
    }

    private void loadWorkoutList() {
        AsyncTask.execute(() -> {
            AppDatabase   db          = Database.getInstance(getContext());
            List<Workout> workoutList = db.workoutDao().getAll();
            requireActivity().runOnUiThread(() -> {
                workoutListAdapter.setContext(getContext());
                workoutListAdapter.setWorkoutList(workoutList);
            });
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // nach Anlegen eines neuen Workouts in der CreateNewWorkoutActivity
        // muss die Liste neu geladen werden, damit das neue Workout auch dargestellt wird
        loadWorkoutList();
    }
}
