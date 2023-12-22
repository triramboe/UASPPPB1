package com.example.calorieuas.admin

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calorieuas.table.FoodItem
import com.example.calorieuas.SessionManager
import com.example.calorieuas.adapter.FoodAdapter
import com.example.calorieuas.dao.FoodItemDao
import com.example.calorieuas.database.AppDatabase
import com.example.calorieuas.databinding.ActivityMainAdminBinding
import com.example.calorieuas.dialog.AdminDialog
import com.example.calorieuas.login.LoginSignUp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivityAdmin : AppCompatActivity() {

    private lateinit var binding: ActivityMainAdminBinding

    lateinit var mFoodItemDao: FoodItemDao
    lateinit var executorService: ExecutorService

    private lateinit var sessionManager: SessionManager

    private lateinit var firebaseAuth: FirebaseAuth
//    private lateinit var firestore: FirebaseFirestore
    val firestore = FirebaseFirestore.getInstance()
    val foodCollection = firestore.collection("food_items")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
//        firestore = FirebaseFirestore.getInstance()

        sessionManager = SessionManager(this)

        // Mendapatkan ID pengguna saat ini
        val userId = firebaseAuth.currentUser?.uid

        // Memeriksa apakah pengguna telah masuk (userId tidak null)
        if (userId != null) {
            // Mendapatkan referensi dokumen pengguna dari Firestore
            val userRef = firestore.collection("users").document(userId)

            // Mengambil data pengguna dari Firestore
            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Mendapatkan data pengguna dari dokumen Firestore
                        val username = document.getString("username")


                        // Menetapkan data pengguna ke TextView di layout
                        binding.textViewAdmin.text = "Hai, $username"

                    }
                }
                .addOnFailureListener { exception ->
                }
        }

        // Menetapkan OnClickListener pada tombol keluar
        binding.logOutAdmin.setOnClickListener {
            // Logout pengguna
            firebaseAuth.signOut()
            sessionManager.clearSession()


            // Navigasi kembali ke halaman login
            val intent = Intent(this, LoginSignUp::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }


        with(binding){
            btnAddMakanan.setOnClickListener{
                showAddFood()
            }
            recyclerAddMakananAdmin.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0 || dy < 0) {
                        addMakananAdmin.visibility = View.GONE
                    }
                }
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        addMakananAdmin.visibility = View.VISIBLE
                    }
                }
            })

        }

        executorService = Executors.newSingleThreadExecutor()
        val db = AppDatabase.getDatabase(this)
        mFoodItemDao = db!!.foodItemDao()!!
    }

    override fun onResume() {
        super.onResume()
        getAllNotes()
    }

    private fun getAllNotes() {
        mFoodItemDao.allFoodItem.observe(this){
            foods ->
            val listAdapter = FoodAdapter(foods){
                selectedFood->
                showAddFood(selectedFood)
            }
            syncDataToFirebase()

            binding.recyclerAddMakananAdmin.apply {
                adapter = listAdapter
                layoutManager = LinearLayoutManager(this@MainActivityAdmin)
            }
        }
    }

    private fun showAddFood(foodItem: FoodItem? = null) {
        val foodDial = AdminDialog(this, foodItem)
        foodDial.show(supportFragmentManager, "Makanan")
    }

    private fun syncDataToFirebase() {
        // Cek koneksi internet
        if (isConnectedToInternet()) {
            executorService.execute {
                // Ambil data yang belum di-sync dari Room
                val unsyncedData = mFoodItemDao.getUnsyncedFoodItems()

                // Kirim data ke Firebase
                for (item in unsyncedData) {
                    // Implementasi logika pengiriman data ke Firebase
                    this.sendDataToFirebase(item)

                    // Setelah berhasil di-sync, tandai data sebagai sudah di-sync
                    mFoodItemDao.updateSyncStatus(item.id, true)
                }
            }
        }
    }

    private fun updateSyncStatusInDatabase(itemId: Long, isSynced: Boolean) {
        executorService.execute {
            mFoodItemDao.updateSyncStatus(itemId, true)
        }
    }

    private fun sendDataToFirebase(foodItem: FoodItem) {
        val data = hashMapOf(
            "itemName" to foodItem.itemName,
            "calories" to foodItem.calories,
            "deskripsi" to foodItem.deskripsi
        )
        foodCollection.add(data).addOnSuccessListener {documentReference->
            val firebaseItemId = documentReference.id
            updateSyncStatusInDatabase(foodItem.id, true)
            executorService.execute {
                mFoodItemDao.updateFirebaseId(foodItem.id, firebaseItemId)
            }
        }.addOnFailureListener {
            // Gagal menambahkan data ke Firebase
            Log.e(TAG, "Gagal menambahkan data ke Firebase", it)
        }
    }

    fun deleteDataFromFirebase(foodItem: FoodItem) {
        if (foodItem.firebaseId != null) {
            foodCollection.document(foodItem.firebaseId).delete()
        }
    }

//    fun updateDataFromFibase(foodItem: FoodItem){
//        if (foodItem.firebaseId != null){
//            foodCollection.document(foodItem.firebaseId).set(foodItem)
//        }
//    }

    fun updateDataFromFirebase(foodItem: FoodItem) {
        if (foodItem.firebaseId != null) {
            val data = hashMapOf(
                "itemName" to foodItem.itemName,
                "calories" to foodItem.calories,
                "deskripsi" to foodItem.deskripsi
            )

            // Gunakan ID Firebase yang sudah ada untuk merujuk dokumen yang akan diupdate
            foodCollection.document(foodItem.firebaseId).set(data)
                .addOnSuccessListener {
                    // Tandai bahwa data sudah di-sync setelah berhasil diupdate
                    updateSyncStatusInDatabase(foodItem.id, true)
                }
                .addOnFailureListener {
                    // Tangani kegagalan untuk mengupdate data di Firebase
                    Log.e(TAG, "Gagal mengupdate data di Firebase", it)
                }
        }
    }



    private fun isConnectedToInternet(): Boolean {


        return true
    }

}