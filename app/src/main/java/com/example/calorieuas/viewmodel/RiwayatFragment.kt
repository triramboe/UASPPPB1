package com.example.calorieuas.viewmodel

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calorieuas.user.ListMakanan
import com.example.calorieuas.R
import com.example.calorieuas.adapter.RiwayatAdapter
import com.google.firebase.auth.FirebaseAuth

class RiwayatFragment : Fragment() {

    private lateinit var makananViewModel: MakananViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_riwayat, container, false)

        val adapter = RiwayatAdapter(requireContext())
        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerViewRiwayatMakanan)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //makananViewModel
        makananViewModel = ViewModelProvider(this).get(MakananViewModel::class.java)
        makananViewModel.readAllData.observe(viewLifecycleOwner, Observer {makanan ->
            adapter.setData(makanan)

        })


        val tambahRiwayatButton: Button = view.findViewById(R.id.tambahRiwayat)
        tambahRiwayatButton.setOnClickListener {
            val intent = Intent(activity, ListMakanan::class.java)
            startActivity(intent)
        }

        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid


        return view
    }





    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RiwayatFragment().apply {
                arguments = Bundle().apply {
                    // Set parameters if needed
                }
            }
    }
}

