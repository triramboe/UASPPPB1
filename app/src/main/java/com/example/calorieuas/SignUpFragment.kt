package com.example.calorieuas

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.calorieuas.databinding.FragmentLoginBinding
import com.example.calorieuas.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val textRegis = binding.textReqLogin

        sessionManager = SessionManager(requireContext())

        textRegis.setOnClickListener{
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.loginViewPager)
            viewPager.currentItem = 0
        }

        val registerBtn = binding.registerBtn
        registerBtn.setOnClickListener{
            registerUser()
        }
        return binding.root
    }

    private fun registerUser() {
        val email = binding.emailField.text.toString().trim()
        val password = binding.passwordField.text.toString().trim()
        val username = binding.usernameField.text.toString().trim()
        val phone = binding.phoneField.text.toString().trim()
        val kaloriTarget = binding.kaloriTargetField.text.toString().trim()
        val bb = binding.bbField.text.toString().trim()
        val tb = binding.tbField.text.toString().trim()

        val checkBox = binding.checkList
        if (!checkBox.isChecked) {
            Toast.makeText(activity, "Harap setujui Syarat dan Ketentuan", Toast.LENGTH_SHORT).show()
            return
        }

        //memeriksa isi field
        if(email.isEmpty() || password.isEmpty() || username.isEmpty()
            || phone.isEmpty() || kaloriTarget.isEmpty() || bb.isEmpty() || tb.isEmpty()
            ){
            Toast.makeText(activity, "Tolong isi semua data yang diperlukan", Toast.LENGTH_SHORT).show()
            return
        }

        //proses pendaftaran
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(requireActivity()){task ->
                if(task.isSuccessful){
                    val userId = firebaseAuth.currentUser?.uid
                    if(userId != null){
                        saveUserDataToFireStore(userId,email, username,phone, kaloriTarget, bb, tb, "user")
                    }
                    // Tampilkan pesan atau navigasi ke fragment profil
                    Toast.makeText(activity, "Pendaftaran Berhasil", Toast.LENGTH_SHORT).show()
                    navigateToMainActivity()
                }
                else{
                    Toast.makeText(
                        activity,
                        "Pendaftaran Gagal: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

    private fun navigateToMainActivity() {
        sessionManager.isLoggedIn = true
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun saveUserDataToFireStore(userId: String, email:String, username: String, phone: String, kaloriTarget: String, bb: String, tb: String, role:String) {
        val user = hashMapOf(
            "email" to email,
            "username" to username,
            "phone" to phone,
            "kaloritarget" to kaloriTarget,
            "bb" to bb,
            "tb" to tb,
            "role" to "user"
         )

        val userRef = firestore.collection("users").document(userId)

        userRef.set(user)
            .addOnSuccessListener {
                Log.d(TAG, "User data added to Firestore")
            }
            .addOnFailureListener{e ->
                Log.w(TAG, "Error adding user data to Firestore")
            }
    }

    companion object {
        private const val TAG = "SignUpFragment"

        @JvmStatic
        fun newInstance() = SignUpFragment()
    }
}