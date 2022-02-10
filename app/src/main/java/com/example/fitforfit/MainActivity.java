package com.example.fitforfit;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.databinding.ActivityMainBinding;
import com.example.fitforfit.entity.Training;
import com.example.fitforfit.singleton.Database;
import com.example.fitforfit.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppDatabase db  = Database.getInstance(getApplicationContext());
        int[]       ids = db.trainingDao().getIdsByWorkoutAndExerciseId(1, 1);
        for (int id : ids) {
            Training training = db.trainingDao().getMaxSetForTrainingId(id);
            Log.d("abcdef", "Gewicht: " + training.weight);
        }
        List<Training> trainingList = db.trainingDao().getMaxWeightSetsByWorkoutAndExerciseId(1, 1);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this);
        ViewPager2           viewPager            = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = binding.tabs;
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText(TAB_TITLES[position])
        ).attach();
    }
}