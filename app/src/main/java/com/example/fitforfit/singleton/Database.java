package com.example.fitforfit.singleton;

import android.content.Context;

import androidx.room.Room;

import com.example.fitforfit.database.AppDatabase;

public class Database {
    static AppDatabase db = null;
    public static String DB_NAME = "fitforfit";

    public static AppDatabase getInstance(Context context) {
        if (db != null) {
            return db;
        }

        db = Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                .allowMainThreadQueries().fallbackToDestructiveMigration()
                /*
                TO DO -> .allowMainThreadQueries() loswerden -> Scheisse für Leistung
                 */
                .build();
        return db;
    }
}
