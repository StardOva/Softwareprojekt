package com.example.fitforfit.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Entity(primaryKeys = {"id", "workout_id", "exercise_id", "set"},
        indices = {
                @Index(value = {"id", "workout_id", "exercise_id", "set"}, unique = true)
        },
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
public class Training {

    public int id;

    @ColumnInfo(name = "workout_id")
    public int workoutId;

    @ColumnInfo(name = "exercise_id")
    public int exerciseId;

    public int set;

    public float weight;

    public int reps;

}
