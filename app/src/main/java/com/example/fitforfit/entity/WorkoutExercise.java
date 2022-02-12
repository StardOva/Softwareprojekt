package com.example.fitforfit.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"workout_id", "exercise_id"},
        foreignKeys = {
                @ForeignKey(
                        entity = Workout.class,
                        parentColumns = "workout_id",
                        childColumns = "workout_id",
                        onDelete = CASCADE
                ),
                @ForeignKey(
                        entity = Exercise.class,
                        parentColumns = "exercise_id",
                        childColumns = "exercise_id",
                        onDelete = CASCADE
                )
        })
public class WorkoutExercise {

    @ColumnInfo(name = "workout_id")
    public int workoutId;

    @ColumnInfo(name = "exercise_id")
    public int exerciseId;

    public int pos;
}
