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
    public float fat;

    @ColumnInfo(name = "saturated_fat")
    public float saturated_fat;

    @ColumnInfo(name = "carb")
    public float carb;

    @ColumnInfo(name = "sugar")
    public float sugar;

    @ColumnInfo(name = "fiber")
    public float fiber;

    @ColumnInfo(name = "protein")
    public float protein;

    @ColumnInfo(name = "salt")
    public float salt;
}
