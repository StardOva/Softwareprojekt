package com.example.fitforfit.ui.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitforfit.R;
import com.example.fitforfit.fragments.AddExerciseToWorkoutFragment;

public class AddExerciseToWorkoutActivity extends AppCompatActivity {

    public AddExerciseToWorkoutActivity() {
        super(R.layout.activity_add_exercise_to_workout);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle                       args     = new Bundle();
            AddExerciseToWorkoutFragment fragment = new AddExerciseToWorkoutFragment();

            args.putInt("workoutId", getIntent().getIntExtra("workoutId", 0));
            fragment.setArguments(args);
            fragment.setParentActivity(this);

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_add_exercise_to_workout, fragment)
                    .commit();
        }
    }
}
