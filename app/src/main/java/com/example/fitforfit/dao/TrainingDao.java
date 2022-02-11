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


    @Query("SELECT t1.*" +
            " FROM training t1 INNER JOIN (" +
            "SELECT id, MAX(weight) weight FROM training GROUP BY id" +
            ") t2 ON t1.id = t2.id AND t1.weight = t2.weight" +
            " WHERE workout_id = :workoutId AND exercise_id = :exerciseId " +
            "ORDER BY t1.id ASC")
    List<Training> getMaxWeightSetsByWorkoutAndExerciseId(int workoutId, int exerciseId);

}
