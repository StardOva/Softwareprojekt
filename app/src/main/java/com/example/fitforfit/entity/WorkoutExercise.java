package com.example.fitforfit.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = Workout.class,
                parentColumns = "id",
                childColumns = "workout_id",
                onDelete = CASCADE
        ),
        @ForeignKey(
                entity = Exercise.class,
                parentColumns = "id",
                childColumns = "exercise_id",
                onDelete = CASCADE
        )
})
public class WorkoutExercise {

    @ColumnInfo(name = "workout_id")
    public int workoutId;

    @ColumnInfo(name = "exercise_id")
    public int exerciseId;
}
