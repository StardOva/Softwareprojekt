package com.example.fitforfit.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Product {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    public int id;

    @ColumnInfo(name = "product_name")
    public String product_name;

    @ColumnInfo(name = "info")
    public String info;

    //in ckal per 100g
    @ColumnInfo(name = "ckal")
    public int ckal;

    //in g per 100g
    @ColumnInfo(name = "fat")
    public int fat;

    @ColumnInfo(name = "saturated_fat")
    public int saturated_fat;

    @ColumnInfo(name = "carb")
    public int carb;

    @ColumnInfo(name = "sugar")
    public int sugar;

    @ColumnInfo(name = "fiber")
    public int fiber;

    @ColumnInfo(name = "protein")
    public int protein;

    @ColumnInfo(name = "salt")
    public int salt;
}
