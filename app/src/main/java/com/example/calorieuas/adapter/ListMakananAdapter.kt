package com.example.calorieuas.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calorieuas.user.KustomMakanan
import com.example.calorieuas.table.MakananItem
import com.example.calorieuas.R

class ListMakananAdapter (private val makananList : ArrayList<MakananItem>) : RecyclerView.Adapter<ListMakananAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_makanan, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val makanan : MakananItem = makananList[position]

        holder.itemName.text = makanan.itemName
        holder.calories.text = makanan.calories
        holder.itemView.findViewById<ImageButton>(R.id.addMakanaUser).setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, KustomMakanan::class.java)

            intent.putExtra("namaMakanan", makanan.itemName)
            intent.putExtra("jmlKaloriMakanan", makanan.calories)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return makananList.size
    }

    public class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val itemName : TextView = itemView.findViewById(R.id.namaMakanan)
        val calories : TextView = itemView.findViewById(R.id.jmlKaloriMakanan)
    }

}
