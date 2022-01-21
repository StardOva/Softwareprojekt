package com.example.fitforfit.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {
        @Index(value = {"id", "date_id"}, unique = true)
},
        foreignKeys = {
                @ForeignKey(
                        entity = Day.class,
                        parentColumns = "date_id",
                        childColumns = "date_id",
                        onDelete = CASCADE
                )
        })
public class Meal {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "meal_id")
    public int id;

    @ColumnInfo(name = "date_id")
    public int date_id;

    @ColumnInfo(name = "meal_name")
    public String meal_name;

    @ColumnInfo(name = "time")
    public String time; //timestamp
}
