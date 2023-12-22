package com.example.calorieuas

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_foods")
data class MakananUser(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Long = 0,

    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "nama_makanan")
    var namaMakanan: String,

    @ColumnInfo(name = "waktu_makan")
    var waktuMakan: String,

    @ColumnInfo(name = "takaran_saji")
    var takaranSaji: Int,

    @ColumnInfo(name = "jumlah_kalori")
    var jumlahKalori: Int
)
