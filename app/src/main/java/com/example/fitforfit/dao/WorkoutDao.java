package com.example.fitforfit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitforfit.entity.Exercise;
import com.example.fitforfit.entity.Workout;

import java.util.List;

@Dao
public interface WorkoutDao {

    @Query("SELECT * FROM workout ORDER BY workout_id DESC")
    List<Workout> getAll();

    @Query("SELECT * FROM workout WHERE workout_id IN (:ids) ORDER BY workout_id DESC")
    List<Workout> loadAllByIds(int[] ids);

    @Query("SELECT * FROM workout WHERE name = :name LIMIT 1")
    Workout findByName(String name);

    @Insert
    void insert(Workout workout);

    @Delete
    void delete(Workout workout);

    @Query("UPDATE workout SET NAME = :newName WHERE workout_id = :id")
    void updateName(int id, String newName);

    @Query("SELECT * FROM exercise e INNER JOIN workoutexercise we ON we.exercise_id = e.exercise_id WHERE we.workout_id = :workoutId ORDER BY we.pos ASC")
    List<Exercise> getRelatedExercises(int workoutId);

}
