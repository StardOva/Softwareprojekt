package com.example.fitforfit.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
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
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);

        // welches Workout wurde angeklickt? -> ID an das Fragment übergeben
        Log.i("abcdef", String.valueOf(getIntent().getIntExtra("workoutId", 0)));

        Intent intent = getIntent();

        WorkoutDetailFragment workoutDetailFragment = new WorkoutDetailFragment();
        workoutDetailFragment.workoutId = getIntent().getIntExtra("workoutId", 0);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_workout_detail, workoutDetailFragment, null)
                    .commit();
        }
    }


}
