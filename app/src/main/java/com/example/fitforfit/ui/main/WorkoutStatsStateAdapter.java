package com.example.fitforfit.ui.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fitforfit.fragments.WorkoutEvaluationFragment;
import com.example.fitforfit.fragments.WorkoutProgressFragment;

public class WorkoutStatsStateAdapter extends FragmentStateAdapter {

    int workoutId;

    public WorkoutStatsStateAdapter(@NonNull FragmentActivity fragmentActivity, int workoutId) {
        super(fragmentActivity);
        this.workoutId = workoutId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new WorkoutEvaluationFragment(workoutId);
            case 1:
                return new WorkoutProgressFragment(workoutId);
        }

        return new WorkoutEvaluationFragment(workoutId);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
