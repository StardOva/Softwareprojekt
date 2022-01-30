package com.example.fitforfit.ui.main;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitforfit.R;
import com.example.fitforfit.fragments.WorkoutDetailFragment;

public class WorkoutDetailActivity extends AppCompatActivity {

    public WorkoutDetailActivity() {
        super(R.layout.activity_workout_detail);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // welches Workout wurde angeklickt? -> ID an das Fragment übergeben
        Bundle                args                  = new Bundle();
        WorkoutDetailFragment workoutDetailFragment = new WorkoutDetailFragment();

        args.putInt("workoutId", getIntent().getIntExtra("workoutId", 0));
        workoutDetailFragment.setArguments(args);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_workout_detail, workoutDetailFragment, null)
                    .commit();
        }
    }


}
