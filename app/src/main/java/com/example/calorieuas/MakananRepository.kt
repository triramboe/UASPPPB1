package com.example.calorieuas

import androidx.lifecycle.LiveData

class MakananRepository(private val makananDao: MakananDao) {
    val readAllData: LiveData<List<MakananUser>> = makananDao.getAllMakanan()

    fun addMakanan (makananUser: MakananUser){
        makananDao.insertMakanan(makananUser)
    }
}