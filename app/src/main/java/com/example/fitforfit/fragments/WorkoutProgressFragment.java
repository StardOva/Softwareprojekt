package com.example.fitforfit.fragments;

import android.annotation.SuppressLint;
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
import com.example.fitforfit.adapter.WorkoutProgressAdapter;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.databinding.FragmentMainBinding;
import com.example.fitforfit.singleton.Database;

import java.util.ArrayList;

public class WorkoutProgressFragment extends Fragment {

    private FragmentMainBinding binding;
    private WorkoutProgressAdapter workoutProgressAdapter;
    private final int workoutId;

    public WorkoutProgressFragment(int workoutId) {
        super(R.layout.fragment_workout_progress);
        this.workoutId = workoutId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);

        return inflater.inflate(R.layout.fragment_workout_progress, binding.getRoot());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initRecyclerView(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initRecyclerView(View view) {
        AsyncTask.execute(() -> {
            RecyclerView recyclerView = view.findViewById(R.id.recyclerViewWorkoutProgress);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            this.workoutProgressAdapter = new WorkoutProgressAdapter(getActivity(), this.workoutId);
            this.workoutProgressAdapter.setParentActivity(getContext());
            recyclerView.setAdapter(this.workoutProgressAdapter);
            loadWorkoutProgressList();
            // wegen Fehler muss das notify() auf UI Thread laufen
            recyclerView.post(() -> workoutProgressAdapter.notifyDataSetChanged());
        });
    }

    private void loadWorkoutProgressList() {
        AppDatabase db = Database.getInstance(getContext());

        int[] ids = db.trainingDao().getAllIds(this.workoutId);
        ArrayList<String> timeStampList = new ArrayList<>();

        for (int id : ids) {
            String createdAt = db.trainingDao().getCreatedAt(id);
            timeStampList.add(createdAt);
        }

        this.workoutProgressAdapter.setWorkoutProgressList(timeStampList, ids);
    }
}
