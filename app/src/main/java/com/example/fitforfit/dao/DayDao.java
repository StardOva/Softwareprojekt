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

    @Query("SELECT * FROM day WHERE date IS NOT NULL ORDER BY day_id DESC LIMIT 1")
    Day getLastDay();

    @Query("SELECT day_id FROM day WHERE date IS NOT NULL ORDER BY day_id DESC LIMIT 1")
    int getLastDayId();

    @Query("SELECT day_id FROM day WHERE date = :date LIMIT 1")
    int getIdByDate(String date);

    @Query("SELECT date FROM day WHERE day_id = :id LIMIT 1")
    String getDateById(int id);

    @Query("UPDATE day SET weight = :weight WHERE day_id = :id")
    void updateWeightById(float weight, int id);

    @Query("UPDATE day SET progress = :newp WHERE day_id = :id")
    void updateProgressById(int newp, int id);

    @Query("SELECT weight FROM day WHERE day_id = :id")
    float getWeightById(int id);

    @Query("SELECT COUNT(day_id) FROM day")
    int getDayIdCount();

    @Query("SELECT weight FROM day WHERE day_id = (SELECT MAX(day_id) FROM day)")
    float getLastWeight();

    @Query("SELECT * FROM day WHERE day_id = :id LIMIT 1")
    Day getDayById(int id);

    @Query("SELECT * FROM day ORDER BY day_id DESC LIMIT 28")
    List<Day> getDaysOfLastMonth();

    @Query("SELECT * FROM day WHERE weight <> :currentWeight ORDER BY day_id DESC LIMIT 1")
    Day getLastWeightDay(float currentWeight);

    @Query("SELECT progress FROM day WHERE day_id = :id")
    int getProgressById(int id);
}
