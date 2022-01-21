package com.example.fitforfit.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.fitforfit.entity.Ingredient;

import java.util.List;

public interface IngredientDao {

    @Query("SELECT * FROM ingredient ORDER BY ingredient_id DESC")
    List<Ingredient> getAllIngredients();

    @Insert
    void insert(Ingredient ingredient);

    @Delete
    void delete(Ingredient ingredient);

    @Update
    void update(Ingredient ingredient);
}
