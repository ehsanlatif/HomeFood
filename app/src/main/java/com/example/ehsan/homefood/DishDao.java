package com.example.ehsan.homefood;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by ehsan on 29-04-2018.
 */

@Dao
public interface DishDao {

    @Query("SELECT * FROM dish")
    List<Dish> getAll();

    @Query("SELECT * FROM dish WHERE chef LIKE :name")
    List<Dish> getChefsDish(String name);

    @Query("SELECT COUNT(*) from dish")
    int countDishes();

    @Insert
    void insertAll(Dish... dishes);

    @Delete
    void delete(Dish dish);

    @Query("DELETE from dish")
    void deleteAll();
}