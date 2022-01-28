package com.example.fitforfit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitforfit.entity.Ingredient;
import com.example.fitforfit.entity.Product;
import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM product ORDER BY product_id DESC")
    List<Product> getAllProducts();

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

    @Query("SELECT * FROM product WHERE product_id = :id LIMIT 1")
    Product findById(int id);

    @Query("SELECT * FROM product WHERE product_id IN (:ids) ORDER BY product_id DESC")
    List<Product> loadAllByIds(int[] ids);

    @Query("DELETE FROM product WHERE product_id = :id")
    void deleteProductById(int id);

    @Query("SELECT MAX(product_id) FROM product")
    int getLastProductId();

    @Query("UPDATE product SET product_name = :name, ckal = :ckal, fat = :fat, saturated_fat = :satfat," +
            "carb = :carb, sugar = :sugar, fiber = :fiber,protein = :protein, " +
            "salt = :salt, info = :info WHERE product_id = :id")
    void updateProductByMealId(String name, int ckal, float fat, float satfat,
                               float carb, float sugar, float fiber,
                               float protein, float salt,String info,
                               int id);

    @Query("DELETE FROM product WHERE product_name = 'NO_PLACEHOLDER'")
    void cleanProducts();

    @Query("SELECT * FROM product WHERE product_id = :id")
    Product getProductById(int id);

}
