package com.example.fitforfit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitforfit.entity.WorkoutExercise;

@Dao
public interface WorkoutExerciseDao {

    @Delete
    void delete(WorkoutExercise workoutExercise);

    @Insert
    void insert(WorkoutExercise workoutExercise);

    @Update
    void update(WorkoutExercise workoutExercise);

    @Query("SELECT pos FROM workoutexercise WHERE workout_id = :workoutId ORDER BY pos DESC LIMIT 1")
    int getLastPosByWorkoutId(int workoutId);
}
