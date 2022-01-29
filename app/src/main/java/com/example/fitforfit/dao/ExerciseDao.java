package com.example.fitforfit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitforfit.entity.Exercise;
import com.example.fitforfit.entity.Workout;

import java.util.List;

@Dao
public interface ExerciseDao {

    @Query("SELECT * FROM exercise ORDER BY name")
    List<Exercise> getAll();

    @Query("SELECT * FROM exercise WHERE exercise_id IN (:ids) ORDER BY exercise_id DESC")
    List<Exercise> loadAllByIds(int[] ids);

    @Query("SELECT * FROM exercise WHERE name = :name LIMIT 1")
    Exercise findByName(String name);

    @Insert
    void insert(Exercise exercise);

    @Delete
    void delete(Exercise exercise);

    @Query("UPDATE exercise SET NAME = :newName WHERE exercise_id = :id")
    void updateName(int id, String newName);

    @Query("SELECT * FROM workout w INNER JOIN workoutexercise we ON we.workout_id = w.workout_id WHERE we.exercise_id = :exerciseId ORDER BY we.pos ASC")
    List<Workout> getRelatedWorkouts(int exerciseId);

}
