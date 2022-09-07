package com.example.fitforfit.singleton;

import android.content.Context;

import androidx.room.Room;

import com.example.fitforfit.database.AppDatabase;

public class Database {
    static AppDatabase db = null;
    public static String DB_NAME = "fitforfit";
    public static String BACKUP_PATH = "/storage/emulated/0/Android/data/com.example.fitforfit/files/backup/fitforfit.sqlite3";
    public static String RESTORE_PATH = "/storage/emulated/0/Android/data/com.example.fitforfit/files/restore/fitforfit.sqlite3";
    public static String DUMP_NAME = "fitforfit.sqlite3";

    public static AppDatabase getInstance(Context context) {
        if (db != null) {
            return db;
        }

        db = Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                .allowMainThreadQueries().fallbackToDestructiveMigration()
                /*
                TODO -> .allowMainThreadQueries() loswerden -> Scheisse für Leistung
                 */
                .build();
        return db;
    }
}
