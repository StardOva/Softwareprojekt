package com.example.fitforfit.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.fitforfit.entity.Meal;

import java.util.List;

public interface MealDao {

    @Query("SELECT * FROM meal ORDER BY meal_id DESC")
    List<Meal> getAllMeals();

    @Insert
    void insert(Meal meal);

    @Delete
    void delete(Meal meal);

    @Update
    void update(Meal meal);
}
