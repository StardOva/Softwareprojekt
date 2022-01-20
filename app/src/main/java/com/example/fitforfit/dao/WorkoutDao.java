package com.example.fitforfit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.fitforfit.entity.Workout;
import com.example.fitforfit.relationship.WorkoutWithExercises;

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

    @Transaction
    @Query("SELECT * FROM workout")
    List<WorkoutWithExercises> getRelatedExercises();

}
