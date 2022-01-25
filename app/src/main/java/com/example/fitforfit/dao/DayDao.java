package com.example.fitforfit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitforfit.entity.Day;
import com.example.fitforfit.entity.Workout;

import java.util.List;

@Dao
public interface DayDao {

    @Query("SELECT * FROM day ORDER BY day_id DESC")
    List<Day> getAllDays();

    @Insert
    void insert(Day day);

    @Delete
    void delete(Day day);

    @Update
    void update(Day day);

    @Query("SELECT date FROM day WHERE date IS NOT NULL ORDER BY day_id DESC LIMIT 1")
    String getLastDate();

    @Query("SELECT day_id FROM day WHERE date = :date LIMIT 1")
    int getIdByDate(String date);

    @Query("SELECT date FROM day WHERE day_id = :id LIMIT 1")
    String getDateById(int id);
}
