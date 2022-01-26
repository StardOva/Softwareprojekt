package com.example.fitforfit.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.fitforfit.dao.DayDao;
import com.example.fitforfit.dao.ExerciseDao;
import com.example.fitforfit.dao.IngredientDao;
import com.example.fitforfit.dao.MealDao;
import com.example.fitforfit.dao.ProductDao;
import com.example.fitforfit.dao.TrainingDao;
import com.example.fitforfit.dao.WorkoutDao;
import com.example.fitforfit.entity.Day;
import com.example.fitforfit.entity.Exercise;
import com.example.fitforfit.entity.Ingredient;
import com.example.fitforfit.entity.Meal;
import com.example.fitforfit.entity.Product;
import com.example.fitforfit.entity.Training;
import com.example.fitforfit.entity.Workout;
import com.example.fitforfit.entity.WorkoutExercise;

@Database(entities = {Exercise.class, Training.class, Workout.class, WorkoutExercise.class, Day.class, Meal.class, Ingredient.class, Product.class}, version = 12)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ExerciseDao exerciseDao();

    public abstract WorkoutDao workoutDao();

    public abstract TrainingDao trainingDao();

    public abstract DayDao dayDao();

    public abstract MealDao mealDao();

    public abstract IngredientDao ingredientDao();

    public abstract ProductDao productDaoDao();
}
