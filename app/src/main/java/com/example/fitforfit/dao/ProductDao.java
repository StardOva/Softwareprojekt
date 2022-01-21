package com.example.fitforfit.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.fitforfit.entity.Product;
import java.util.List;

public interface ProductDao {

    @Query("SELECT * FROM product ORDER BY product_id DESC")
    List<Product> getAllMeals();

    @Insert
    void insert(Product product);

    @Delete
    void delete(Product product);

    @Update
    void update(Product product);

    @Query("SELECT * FROM product WHERE product_name LIKE :name")
    Product findByManyName(String name);

    @Query("SELECT * FROM product WHERE product_name = :name LIMIT 1")
    Product findByOneName(String name);

    @Query("SELECT * FROM product WHERE product_id IN (:ids) ORDER BY product_id DESC")
    List<Product> loadAllByIds(int[] ids);
}
