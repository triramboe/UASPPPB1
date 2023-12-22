package com.example.calorieuas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calorieuas.table.FoodItem
import com.example.calorieuas.databinding.ItemMakananAdminBinding

class FoodAdapter (private val listFood: List<FoodItem>,
                   private val onItemClickListener: (FoodItem)->Unit)
    :RecyclerView.Adapter<FoodAdapter.ItemFoodViewHolder>(){
    inner class ItemFoodViewHolder(private val binding: ItemMakananAdminBinding ):
        RecyclerView.ViewHolder(binding.root){
            fun bind(foodItem: FoodItem){
                with(binding){
                    namaMakananAdm.text = foodItem.itemName
                    jmlKaloriAdm.text  = foodItem.calories
                }
            }
        }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemFoodViewHolder {
        val binding = ItemMakananAdminBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemFoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemFoodViewHolder, position: Int) {
        holder.bind(listFood[position])
        holder.itemView.setOnClickListener{
            onItemClickListener.invoke(listFood[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return listFood.size
    }
}