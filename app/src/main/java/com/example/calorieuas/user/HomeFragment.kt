package com.example.calorieuas.user

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.example.calorieuas.R
import com.example.calorieuas.adapter.SliderAdapter
import com.example.calorieuas.dao.MakananDao
import com.example.calorieuas.database.MakananDatabase
import com.example.calorieuas.databinding.FragmentHomeBinding
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var vSlider : ViewPager
    lateinit var tanggalTextView: TextView

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var makananDao: MakananDao
    private lateinit var userId: String

    private val CHANNEL_ID = "CalorieNotificationChannel"
    private val CHANNEL_NAME = "Calorie Notification Channel"
    private val PREFS_NAME = "CaloriePrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        vSlider =view.findViewById(R.id.home_slider)
        tanggalTextView = view.findViewById(R.id.homeTanggal)

        makananDao = MakananDatabase.getDatabase(requireContext())?.makananDao()!!
        userId = firebaseAuth.currentUser?.uid ?: ""

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val totalCalories = makananDao.getTotalCalories(userId)
                withContext(Dispatchers.Main) {
                    binding.valueKonsumsiKalori.text = totalCalories.toString()
                }
            } catch (e: Exception) {
                // Handle the exception (e.g., log it, show an error message)
                withContext(Dispatchers.Main) {
                    // Display an error message or handle it appropriately
                }
            }
        }



        // Mendapatkan ID pengguna saat ini
        val userId = firebaseAuth.currentUser?.uid

        // Memeriksa apakah pengguna telah masuk (userId tidak null)
        if (userId != null) {
            // Mendapatkan referensi dokumen pengguna dari Firestore
            val userRef = firestore.collection("users").document(userId)

            // Mengambil data pengguna dari Firestore
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val totalCalories = makananDao.getTotalCalories(userId)
                    val document = userRef.get().await()

                    withContext(Dispatchers.Main) {
                        if (document.exists()) {
                            val username = document.getString("username")
                            val target = document.getString("kaloritarget")
                            val totalCalori = totalCalories

                            // Set UI elements on the main thread
                            binding.homeWelcome.text = "Hai, $username"
                            binding.valueTargetKalori.text = "$target"

                            val sisaKalori = target!!.toInt() - totalCalori
                            binding.valueSisaKalori.text = "$sisaKalori"

                            var persentarget = (totalCalori.toDouble() /target!!.toInt()) * 100
                            if (persentarget > 100){
                                persentarget = 100.0
                            }

                            binding.progressBarHome.progress = persentarget.toInt()

                            clearNotificationStatus()

                            // Check if consumed calories exceed target calories
                            if (totalCalori > target.toInt() && !isNotificationShown()) {
                                showNotification("Calorie Alert", "You have exceeded your target calories!")
                                Toast.makeText(
                                    requireContext(),
                                    "Calorie Alert: You have exceeded your target calories!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                markNotificationAsShown()
                            }
                        }
                    }
                } catch (e: Exception) {
                    // Handle exceptions
                    withContext(Dispatchers.Main) {
                        // Display an error message or handle it appropriately
                    }
                }
            }
        }




        val arraySlider = ArrayList<Int>()

        arraySlider.add(R.drawable.oatmeal)
        arraySlider.add(R.drawable.pancake)
        arraySlider.add(R.drawable.ramen)
        arraySlider.add(R.drawable.sanwidch)
        arraySlider.add(R.drawable.stroberi)
        arraySlider.add(R.drawable.supkari)//tambah item dan gambar

        val sliderAdapter = SliderAdapter(arraySlider, activity)
        vSlider.adapter = sliderAdapter

        // Set up current date for the date TextView
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        tanggalTextView.text = formattedDate


        return view
    }

    private fun clearNotificationStatus() {
        val sharedPreferences =
            requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("isNotificationShown")
        editor.apply()
    }

    private fun markNotificationAsShown() {
        val sharedPreferences =
            requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isNotificationShown", true)
        editor.apply()
    }

    private fun isNotificationShown(): Boolean {
        val sharedPreferences =
            requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isNotificationShown", false)
    }


    private fun showNotification(title: String, content: String) {
        // Create an explicit intent for the notification
        // (You can customize this intent based on your app's behavior)
        // For example, you can open a specific activity when the notification is clicked.
        val intent = Intent(requireContext(), MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Create a notification
        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notif)
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        // Create a notification manager
        val notificationManager =
            NotificationManagerCompat.from(requireContext())

        // Create a notification channel (required for API 26 and above)
        createNotificationChannel(notificationManager)

        // Show the notification
        notificationManager.notify(1, builder.build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManagerCompat) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Calorie notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getTargetCalories(): Task<DocumentSnapshot> {
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        return user?.uid?.let { userId ->
            db.collection("users").document(userId)
                .get()
        } ?: Tasks.forException(Exception("User not logged in"))
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}


