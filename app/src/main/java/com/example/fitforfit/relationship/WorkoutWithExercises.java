package com.example.fitforfit.relationship;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.fitforfit.entity.Exercise;
import com.example.fitforfit.entity.Workout;
import com.example.fitforfit.entity.WorkoutExercise;

import java.util.List;

/**
 * Represents a workout with all related exercises
 */
public class WorkoutWithExercises {

    @Embedded public Workout workout;

    @Relation(
            parentColumn = "workout_id",
            entityColumn = "exercise_id",
            associateBy = @Junction(WorkoutExercise.class)
    )
    public List<Exercise> exerciseList;

}
