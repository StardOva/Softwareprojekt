package com.example.fitforfit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitforfit.entity.Exercise;
import com.example.fitforfit.entity.Workout;

import java.util.List;

@Dao
public interface ExerciseDao {

    @Query("SELECT * FROM exercise ORDER BY name")
    List<Exercise> getAll();

    @Query("SELECT e.* FROM exercise e LEFT JOIN workoutexercise we ON e.exercise_id = we.exercise_id WHERE " +
            "(we.workout_id IS NULL OR we.workout_id != :workoutId)")
    List<Exercise> getUnusedExercisesForWorkout(int workoutId);

    @Query("SELECT e.* FROM exercise e LEFT JOIN workoutexercise we ON e.exercise_id = we.exercise_id WHERE we.workout_id = :workoutId")
    List<Exercise> getUsedExercisesForWorkout(int workoutId);

    @Query("SELECT * FROM exercise WHERE exercise_id IN (:ids) ORDER BY exercise_id DESC")
    List<Exercise> loadAllByIds(int[] ids);

    @Query("SELECT * FROM exercise WHERE exercise_id = :id")
    Exercise getById(int id);

    @Insert
    void insert(Exercise exercise);

    @Delete
    void delete(Exercise exercise);

    @Update
    void update(Exercise exercise);

    @Query("SELECT * FROM workout w INNER JOIN workoutexercise we ON we.workout_id = w.workout_id WHERE we.exercise_id = :exerciseId ORDER BY we.pos ASC")
    List<Workout> getRelatedWorkouts(int exerciseId);

}
