package com.example.fitforfit.entity;

import static androidx.room.ForeignKey.CASCADE;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {
        @Index(value = {"ingredient_id", "meal_id", "product_id"}, unique = true)
},
        foreignKeys = {
                @ForeignKey(
                        entity = Meal.class,
                        parentColumns = "meal_id",
                        childColumns = "meal_id",
                        onDelete = CASCADE
                ),
                @ForeignKey(
                        entity = Product.class,
                        parentColumns = "product_id",
                        childColumns = "product_id",
                        onDelete = CASCADE
                )
        })
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ingredient_id")
    public int id;

    @ColumnInfo(name = "meal_id")
    public int meal_id;

    @ColumnInfo(name = "product_id")
    public int product_id;

    @ColumnInfo(name = "ingredient_name")
    public String ingredient_name;

    @ColumnInfo(name = "quantity")
    public int quantity;
}
