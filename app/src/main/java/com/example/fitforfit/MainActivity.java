package com.example.fitforfit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.databinding.ActivityMainBinding;
import com.example.fitforfit.entity.Exercise;
import com.example.fitforfit.entity.WorkoutExercise;
import com.example.fitforfit.singleton.Database;
import com.example.fitforfit.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this);
        ViewPager2           viewPager            = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = binding.tabs;
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText(TAB_TITLES[position])
        ).attach();

        // TODO wieder rausnehmen
        AsyncTask.execute(() -> {
            /*
            AppDatabase db = Database.getInstance(getApplicationContext());

            WorkoutExercise workoutExercise = new WorkoutExercise();
            workoutExercise.workoutId = 1;
            workoutExercise.exerciseId = 1;
            workoutExercise.pos = 1;

            db.workoutExerciseDao().insert(workoutExercise);
             */
        });

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}