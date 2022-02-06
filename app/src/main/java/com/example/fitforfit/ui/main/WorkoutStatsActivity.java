package com.example.fitforfit.ui.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.fitforfit.R;
import com.example.fitforfit.databinding.ActivityWorkoutStatsBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class WorkoutStatsActivity extends AppCompatActivity {

    private ActivityWorkoutStatsBinding binding;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.workout_stats_tab1, R.string.workout_stats_tab2};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWorkoutStatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int workoutId = getIntent().getIntExtra("workoutId", 0);

        WorkoutStatsStateAdapter adapter   = new WorkoutStatsStateAdapter(this, workoutId);
        ViewPager2               viewPager = binding.workoutStatsViewPager;
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = binding.workoutStatsTabs;
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText(TAB_TITLES[position])
        ).attach();
    }
}
