package com.example.fitforfit.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {
        @Index(value = {"name"}, unique = true)
})
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
}
