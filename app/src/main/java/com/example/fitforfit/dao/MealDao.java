package com.example.fitforfit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.fitforfit.entity.Meal;

import java.util.List;

@Dao
public interface MealDao {

    @Query("SELECT * FROM meal ORDER BY meal_id DESC")
    List<Meal> getAllMeals();

    @Query("SELECT * FROM meal WHERE day_id = :id ORDER BY meal_id DESC")
    List<Meal> getAllMealsOnDay(int id);

    @Query("SELECT MAX(meal_id) FROM meal")
    int getLastMealId();

    @Query("DELETE FROM meal WHERE meal_id = :id")
    void deleteMealById(int id);

    @Insert
    void insert(Meal meal);

    @Delete
    void delete(Meal meal);

    @Update
    void update(Meal meal);

    @Query("UPDATE meal SET meal_name = :name, time = :time WHERE meal_id = :meal_id")
    void updateMealNameTimeByMealId(String name, String time, int meal_id);
}
