package com.example.fitforfit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitforfit.entity.Training;

@Dao
public interface TrainingDao {

    @Insert
    void insert(Training training);

    @Delete
    void delete(Training training);

    @Update
    void update(Training training);

    @Query("SELECT MAX(id) FROM training")
    int getLastId();

}
