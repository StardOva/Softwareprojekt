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
}
