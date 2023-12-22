import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calorieuas.LoginSignUp
import com.example.calorieuas.SessionManager
import com.example.calorieuas.databinding.FragmentProfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfilFragment : Fragment() {

    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        val view = binding.root

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        sessionManager = SessionManager(requireContext())

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
                        val email = document.getString("email")
                        val phone = document.getString("phone")
                        val tb = document.getString("tb")
                        val bb = document.getString("bb")

                        // Menetapkan data pengguna ke TextView di layout
                        binding.usernameProfil.text = username
                        binding.emailProfile.text = email
                        binding.nomorProfil.text = phone
                        binding.tbProiile.text = tb
                        binding.bbProfiile.text = bb
                    }
                }
                .addOnFailureListener { exception ->
                    // Penanganan kesalahan jika gagal mengambil data
                    // Misalnya, tampilkan pesan kesalahan atau log kesalahan
                }
        }

        // Menetapkan OnClickListener pada tombol keluar
        binding.btnLogOutProfile.setOnClickListener {
            firebaseAuth.signOut()
            sessionManager.clearSession()



            // Navigasi kembali ke halaman login
            val intent = Intent(activity, LoginSignUp::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            activity?.finish()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
