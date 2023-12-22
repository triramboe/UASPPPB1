package com.example.calorieuas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.calorieuas.databinding.FragmentHomeBinding
import com.example.calorieuas.databinding.FragmentProfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
                        binding.homeWelcome.text = "Hai, $username"

                    }
                }
                .addOnFailureListener { exception ->
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}