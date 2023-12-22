package com.example.calorieuas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.calorieuas.table.MakananUser

@Dao
interface MakananDao {
    @Insert
     fun insertMakanan(makanan: MakananUser)

    @Update
     fun updateMakanan(makanan: MakananUser)

    @Delete
     fun deleteMakanan(makanan: MakananUser)

    @Query("SELECT * FROM custom_foods WHERE userId = :userId")
     fun getMakananByUserId(userId: String): LiveData<List<MakananUser>>

    @Query("SELECT * FROM custom_foods")
     fun getAllMakanan(): LiveData<List<MakananUser>>

    @Query("SELECT SUM(jumlah_kalori) FROM custom_foods WHERE userId = :userId")
    fun getTotalCalories(userId: String): Int

    @Query("SELECT * FROM custom_foods WHERE userId = :userId AND nama_makanan LIKE :searchQuery")
    fun searchMakananByUserId(userId: String, searchQuery: String): LiveData<List<MakananUser>>



}
