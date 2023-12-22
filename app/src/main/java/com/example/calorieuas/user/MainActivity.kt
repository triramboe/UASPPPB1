package com.example.calorieuas.user

import com.example.calorieuas.adapter.ProfilFragment
import com.example.calorieuas.viewmodel.RiwayatFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.calorieuas.R
import com.example.calorieuas.dao.MakananDao
import com.example.calorieuas.database.MakananDatabase
import com.example.calorieuas.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var mMakananDao: MakananDao
    lateinit var executorService: ExecutorService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        executorService = Executors.newSingleThreadExecutor()
        val db = MakananDatabase.getDatabase(this)
        mMakananDao = db!!.makananDao()!!

        binding.bottomNavigationView.selectedItemId = R.id.home
        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment())
                R.id.profil -> replaceFragment(ProfilFragment())
                R.id.riwayat -> replaceFragment(RiwayatFragment())

                else->{



                }
            }

            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_main,fragment)
        fragmentTransaction.commit()

    }
}