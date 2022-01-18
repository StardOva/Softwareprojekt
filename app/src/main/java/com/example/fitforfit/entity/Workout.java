package com.example.fitforfit.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {
        @Index(value = {"name"}, unique = true)
})
public class Workout {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    /* TODO Timestamps für jede Entity implementieren
    @ColumnInfo(name = "created_at", defaultValue = "(datetime('now'))")
    private String createdAtString;

    @Ignore
    private LocalDateTime createdAt;

    public LocalDateTime getCreatedAt() {
        return LocalDateTime.parse(this.createdAtString);
    }
    */
}
