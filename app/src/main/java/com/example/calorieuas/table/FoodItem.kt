package com.example.calorieuas.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_items")
data class FoodItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var itemName: String = "", // Provide default values for the properties
    var calories: String = "",
    var deskripsi: String = "",
    val isSynced: Boolean = false,
    val firebaseId: String? = null
) {
    // Add a no-argument constructor
    constructor() : this(0, "", "", "", false, null)
}
