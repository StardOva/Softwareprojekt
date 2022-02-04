package com.example.fitforfit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.fitforfit.entity.Ingredient;

import java.util.List;

@Dao
public interface IngredientDao {

    @Query("SELECT * FROM ingredient ORDER BY ingredient_id DESC")
    List<Ingredient> getAllIngredients();

    @Insert
    void insert(Ingredient ingredient);

    @Delete
    void delete(Ingredient ingredient);

    @Update
    void update(Ingredient ingredient);

    @Query("SELECT * FROM ingredient WHERE meal_id = :mealId")
    List<Ingredient> getAllIngredientsOnMeal(int mealId);

    @Query("SELECT * FROM ingredient WHERE ingredient_id = :id")
    Ingredient getIngredientById(int id);

    @Query("SELECT product_id FROM Ingredient WHERE ingredient_id = :id")
    int selectProdId(int id);

    @Query("DELETE FROM ingredient WHERE ingredient_id = :id")
    void deleteIngredientById(int id);

    @Query("DELETE FROM ingredient WHERE meal_id = :id")
    void deleteAllIngredientByMealId(int id);

    @Query("UPDATE ingredient SET ingredient_name = :name, quantity = :quant WHERE ingredient_id = :id")
    void updateNameQuantityById(String name, int quant, int id);


}
