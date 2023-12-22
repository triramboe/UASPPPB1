package com.example.calorieuas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.calorieuas.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        val textRegis = binding.textReqRegis
        val loginButton = binding.loginBtn

        sessionManager = SessionManager(requireContext())

        textRegis.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.loginViewPager)
            viewPager.currentItem = 1
        }

        loginButton.setOnClickListener {
            val email = binding.logEmailField.text.toString()
            val password = binding.logPassField.text.toString()

            // Melakukan proses login menggunakan Firebase Authentication
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Jika login berhasil, atur status login ke true
                        sessionManager.isLoggedIn = true
                        // Jika login berhasil, cek role pengguna dan navigasikan ke halaman yang sesuai
                        checkUserRole()
                    } else {
                        // Jika login gagal, tampilkan pesan kesalahan
                        Toast.makeText(
                            activity,
                            "Login Gagal: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        return view
    }

    private fun checkUserRole() {
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            val userRef = firestore.collection("users").document(userId)

            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val role = document.getString("role")

                        sessionManager.userRole = role

                        // Navigasi ke halaman sesuai dengan role pengguna
                        if (role == "admin") {
                            // Navigasi ke halaman admin
                            startActivity(Intent(activity, MainActivityAdmin::class.java))
                            activity?.finish() // Optional: Menutup aktivitas login agar tidak bisa kembali ke halaman login
                        } else {
                            // Navigasi ke halaman user
                            startActivity(Intent(activity, MainActivity::class.java))
                            activity?.finish() // Optional: Menutup aktivitas login agar tidak bisa kembali ke halaman login
                        }
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        }
    }

    companion object {
        private const val TAG = "LoginFragment"

        @JvmStatic
        fun newInstance() = LoginFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
