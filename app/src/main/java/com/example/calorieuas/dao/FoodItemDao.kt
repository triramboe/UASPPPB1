package com.example.calorieuas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.calorieuas.table.FoodItem

@Dao
interface FoodItemDao {
    @Insert
    fun insert(foodItem: FoodItem)

    @Update
    fun update(foodItem: FoodItem)

    @Delete
    fun delete(foodItem: FoodItem)

    @Query("SELECT * FROM food_items")
    fun getAllFoodItems(): List<FoodItem>

    @Query("SELECT * FROM food_items WHERE id = :itemId")
    fun getFoodItemById(itemId: Long): FoodItem?

    @get:Query("SELECT * from food_items ORDER BY id ASC")
    val allFoodItem: LiveData<List<FoodItem>>

    @Query("SELECT * FROM food_items WHERE isSynced = 0")
    fun getUnsyncedFoodItems(): List<FoodItem>

    @Query("UPDATE food_items SET isSynced = :isSynced WHERE id = :itemId")
    fun updateSyncStatus(itemId: Long, isSynced: Boolean)

    @Query("UPDATE food_items SET firebaseId = :firebaseId WHERE id = :itemId")
    fun updateFirebaseId(itemId: Long, firebaseId: String)


}