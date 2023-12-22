package com.example.calorieuas.viewmodel

import androidx.lifecycle.LiveData
import com.example.calorieuas.dao.MakananDao
import com.example.calorieuas.table.MakananUser

class MakananRepository(private val makananDao: MakananDao) {
    val readAllData: LiveData<List<MakananUser>> = makananDao.getAllMakanan()

    fun addMakanan (makananUser: MakananUser){
        makananDao.insertMakanan(makananUser)
    }
}