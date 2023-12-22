package com.example.calorieuas.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.calorieuas.table.FoodItem
import com.example.calorieuas.admin.MainActivityAdmin
import com.example.calorieuas.databinding.DetailMakananBinding

class AdminDialog (private val activity: MainActivityAdmin, private val foodItem : FoodItem? = null):
DialogFragment(){
    val binding by lazy {
        DetailMakananBinding.inflate(layoutInflater)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.apply {
            if (foodItem == null){
                setPositiveButton("Tambah"){
                    dialog, which ->
                    insert(
                        FoodItem(
                        itemName = binding.editMakananName.text.toString(),
                        calories = binding.editJumlahKalori.text.toString(),
                        deskripsi = binding.editDeskripsiMakanan.text.toString())
                    )
                    dismiss()
                }
            }
            else{
                with(binding){
                    editMakananName.setText(foodItem.itemName)
                    editJumlahKalori.setText(foodItem.calories)
                    editDeskripsiMakanan.setText(foodItem.deskripsi)
                }

                setPositiveButton("Edit"){
                    dialog, which ->
                    foodItem.itemName = binding.editMakananName.text.toString()
                    foodItem.calories = binding.editJumlahKalori.text.toString()
                    foodItem.deskripsi = binding.editDeskripsiMakanan.text.toString()
                    update(foodItem)
                    dismiss()
                }
                setNeutralButton("Hapus"){
                    dialog, which ->
                    delete(foodItem)
                    dismiss()
                }
            }
        }
        builder.setView(binding.root).setTitle("Daftar Makanan")
        return builder.create()
    }
    private fun insert(foodItem: FoodItem) {
        activity.executorService.execute {
            activity.mFoodItemDao.insert(foodItem = foodItem)
            activity.mFoodItemDao.updateSyncStatus(foodItem.id, false)
        }
    }
    private fun update(foodItem: FoodItem) {
        activity.executorService.execute {
            activity.mFoodItemDao.update(foodItem = foodItem)
            activity.updateDataFromFirebase(foodItem)
        }
    }
    private fun delete(foodItem: FoodItem) {
        activity.executorService.execute {
            activity.mFoodItemDao.delete(foodItem = foodItem)
            activity.deleteDataFromFirebase(foodItem)
        }
    }

}