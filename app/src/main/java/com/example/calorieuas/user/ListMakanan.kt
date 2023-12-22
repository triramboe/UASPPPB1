package com.example.calorieuas.user

import com.example.calorieuas.adapter.ListMakananAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calorieuas.databinding.ActivityListMakananBinding
import com.example.calorieuas.table.MakananItem
import com.example.calorieuas.user.KustomMakanan
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class ListMakanan : AppCompatActivity() {

    private lateinit var binding: ActivityListMakananBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var makananArrayList: ArrayList<MakananItem>
    private lateinit var makananAdapter: ListMakananAdapter
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListMakananBinding.inflate(layoutInflater)
        setContentView(binding.root)


        recyclerView = binding.recyclerViewMakanan
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        makananArrayList = arrayListOf()
        makananAdapter = ListMakananAdapter(makananArrayList)

        recyclerView.adapter = makananAdapter

        EventChangeListener()



        with(binding) {

            customMakananUser.setOnClickListener {
                // Menggunakan Intent untuk berpindah ke KustomMakanan activity
                val intent = Intent(this@ListMakanan, KustomMakanan::class.java)
                startActivity(intent)
            }

            recyclerViewMakanan.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0 || dy < 0) {
                        customMakananUser.visibility = View.GONE
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        customMakananUser.visibility = View.VISIBLE
                    }
                }
            })
            btnBackRiwayat.setOnClickListener{
                val fragment = supportFragmentManager.findFragmentByTag("yourFragmentTag")
                if (fragment != null && fragment.isVisible) {
                    supportFragmentManager.popBackStack()
                } else {
                    finish()
                }
            }

        }

    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("food_items")
            .addSnapshotListener(object : com.google.firebase.firestore.EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("firestore Error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            makananArrayList.add(dc.document.toObject(MakananItem::class.java))
                        }
                    }

                    makananAdapter.notifyDataSetChanged()
                }
            })
    }



}
