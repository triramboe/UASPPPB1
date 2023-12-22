package com.example.calorieuas.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.calorieuas.table.FoodItem
import com.example.calorieuas.dao.FoodItemDao
import kotlinx.coroutines.InternalCoroutinesApi

@Database(entities = [FoodItem::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodItemDao(): FoodItemDao?

    companion object {
        @Volatile
        private var INSTANCE : AppDatabase? = null
        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context) : AppDatabase?{
            if (INSTANCE == null){
                synchronized(AppDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "app_database"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }

    }
}