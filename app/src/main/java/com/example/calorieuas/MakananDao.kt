package com.example.calorieuas

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

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


}
