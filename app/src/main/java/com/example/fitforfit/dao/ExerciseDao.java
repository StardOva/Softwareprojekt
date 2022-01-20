package com.example.fitforfit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.fitforfit.entity.Exercise;
import com.example.fitforfit.relationship.ExerciseWithWorkouts;

import java.util.List;

@Dao
public interface ExerciseDao {

    @Query("SELECT * FROM exercise ORDER BY exercise_id DESC")
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

    @Transaction
    @Query("SELECT * FROM exercise")
    List<ExerciseWithWorkouts> getRelatedWorkouts();

}
