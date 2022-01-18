package com.example.fitforfit.relationship;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.fitforfit.entity.Exercise;
import com.example.fitforfit.entity.Workout;
import com.example.fitforfit.entity.WorkoutExercise;

import java.util.List;

public class ExerciseWithWorkouts {

    @Embedded
    public Exercise exercise;

    @Relation(
            parentColumn = "exercise_id",
            entityColumn = "workout_id",
            associateBy = @Junction(WorkoutExercise.class)
    )
    public List<Workout> workoutList;
}
