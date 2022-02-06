package com.example.fitforfit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitforfit.entity.Training;

import java.util.List;

@Dao
public interface TrainingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Training training);

    @Delete
    void delete(Training training);

    @Update
    void update(Training training);

    @Query("SELECT MAX(id) FROM training")
    int getLastId();

    @Query("SELECT DISTINCT id FROM training WHERE workout_id = :workoutId ORDER BY ID DESC")
    int[] getAllIds(int workoutId);

    @Query("SELECT created_at from training WHERE id = :id")
    String getCreatedAt(int id);

    @Query("SELECT * FROM training WHERE workout_id = :workoutId AND id = :trainingId AND exercise_id = :exerciseId ORDER BY `set` ASC")
    List<Training> getAllSetsByWorkoutAndTrainingAndExerciseId(int workoutId, int trainingId, int exerciseId);

}
