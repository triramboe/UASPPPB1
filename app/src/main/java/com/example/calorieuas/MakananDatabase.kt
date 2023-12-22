package com.example.calorieuas

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi

@Database(entities = [MakananUser::class], version = 1)
abstract class MakananDatabase : RoomDatabase() {
    abstract fun makananDao(): MakananDao?

    companion object {
        @Volatile
        private var INSTANCE : MakananDatabase ? = null
        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context) : MakananDatabase?{
            if (INSTANCE == null){
                synchronized(MakananDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MakananDatabase::class.java, "makanan_database"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }

    }
}