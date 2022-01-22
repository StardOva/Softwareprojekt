package com.example.fitforfit.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Day {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "day_id")
    public int id;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "progress")
    public int progress; //in %
}
