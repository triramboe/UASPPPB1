package com.example.calorieuas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.calorieuas.table.MakananUser
import com.example.calorieuas.database.MakananDatabase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch  // Make sure this import statement is correct

class MakananViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<MakananUser>>
    private val repository: MakananRepository

    init {
        val makananDao = MakananDatabase.getDatabase(application)?.makananDao()
        repository = makananDao?.let { MakananRepository(it) }!!
        readAllData = repository.readAllData
    }

    fun addMakanan(makananUser: MakananUser) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMakanan(makananUser)
        }
    }
}
