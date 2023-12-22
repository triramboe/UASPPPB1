package com.example.calorieuas

import RiwayatFragment
import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.calorieuas.databinding.ActivityKustomMakananBinding
import com.google.firebase.auth.FirebaseAuth

class KustomMakanan : AppCompatActivity() {

    private lateinit var binding: ActivityKustomMakananBinding

    private lateinit var makananViewModel: MakananViewModel

    val firebaseAuth = FirebaseAuth.getInstance()
    val userId = firebaseAuth.currentUser?.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKustomMakananBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val mealTypes = listOf<String>("Makan Pagi", "Makan Siang", "Makan Malam")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, mealTypes)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinnerMealType.adapter = adapter

        val itemName = intent.getStringExtra("namaMakanan")
        val calorie = intent.getStringExtra("jmlKaloriMakanan")
        with(binding){
            makananNameUser.setText(itemName)
            editJumlahKalori.setText(calorie)
        }

        makananViewModel = ViewModelProvider(this).get(MakananViewModel::class.java)


        binding.btnBackListMakanan.setOnClickListener {
            val intent = Intent(this, ListMakanan::class.java)
            startActivity(intent)
            finish()
        }

        binding.simpanKustomMakanan.setOnClickListener{
            saveMakananToDatabase()

        }
    }

    private fun saveMakananToDatabase() {
        val userID = userId
        val namaMakanan = binding.makananNameUser.text.toString()
        val waktuMakan = binding.spinnerMealType.selectedItem.toString()
        val takaranSaji = binding.editberatMakanan.text.toString().toInt()
        val jumlahKaloriPer100g = binding.editJumlahKalori.text.toString().toInt()
        val jumlahKalori = (takaranSaji / 100) * jumlahKaloriPer100g

        val makanan = userID?.let {
            MakananUser(
                userId = it,
                namaMakanan = namaMakanan,
                waktuMakan = waktuMakan,
                takaranSaji = takaranSaji,
                jumlahKalori = jumlahKalori
            )
        }
        if (makanan != null) {
            makananViewModel.addMakanan(makanan)
        }
        Toast.makeText(this,"Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)


    }
}
