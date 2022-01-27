package com.example.fitforfit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
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

    @Query("UPDATE product SET product_name = :name, protein = :protein, info = :info WHERE product_id = :id")
    void updateProductByMealId(String name,float protein, String info, int id);


}
