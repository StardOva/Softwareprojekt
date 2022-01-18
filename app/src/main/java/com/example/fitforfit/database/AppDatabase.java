package com.example.fitforfit.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.fitforfit.dao.ExerciseDao;
import com.example.fitforfit.dao.TrainingDao;
import com.example.fitforfit.dao.WorkoutDao;
import com.example.fitforfit.entity.Exercise;
import com.example.fitforfit.entity.Training;
import com.example.fitforfit.entity.Workout;
import com.example.fitforfit.entity.WorkoutExercise;

@Database(entities = {Exercise.class, Training.class, Workout.class, WorkoutExercise.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ExerciseDao exerciseDao();

    public abstract WorkoutDao workoutDao();

    public abstract TrainingDao trainingDao();
}
