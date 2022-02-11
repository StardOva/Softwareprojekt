package com.example.fitforfit.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"workout_id", "exercise_id"})
public class WorkoutExercise {

    @ColumnInfo(name = "workout_id")
    public int workoutId;

    @ColumnInfo(name = "exercise_id")
    public int exerciseId;

    public int pos;
}
