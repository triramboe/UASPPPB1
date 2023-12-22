package com.example.calorieuas

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.DialogFragment
import androidx.room.ColumnInfo
import com.example.calorieuas.databinding.RiwayatMakananBinding

class RiwayatDialog (private val activity: MainActivity, private  val makanan: MakananUser): DialogFragment() {
    val binding by lazy {
        RiwayatMakananBinding.inflate(layoutInflater)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            with(binding) {
                mealType.setText(makanan.waktuMakan)
                makananNameUser.setText(makanan.namaMakanan)
                editTakaran.setText(makanan.takaranSaji.toString())

                // Convert editTakaran.text to a numeric type (e.g., Double) for calculation
                val newTakaran = editTakaran.text.ifEmpty { 0 }.toString().toInt()
                val takaranValue = makanan.takaranSaji
                val jumlahKalori = (makanan.jumlahKalori / takaranValue) * newTakaran
                // Set the calculated value to editJumlahKalori
                editJumlahKalori.setText(jumlahKalori.toString())

                editTakaran.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
//                        TODO("Not yet implemented")
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
//                        TODO("Not yet implemented")
                    }

                    override fun afterTextChanged(s: Editable?) {
//                        TODO("Not yet implemented")

                        // Convert editTakaran.text to a numeric type (e.g., Double) for calculation
                        val newTakaran = editTakaran.text.ifEmpty { 0 }.toString().toInt()
                        val takaranValue = makanan.takaranSaji
                        val jumlahKalori = (makanan.jumlahKalori / takaranValue) * newTakaran
                        // Set the calculated value to editJumlahKalori
                        editJumlahKalori.setText(jumlahKalori.toString())
                    }

                })
            }
            setPositiveButton("Simpan")
            { dialog, which ->
                makanan.namaMakanan = binding.makananNameUser.text.toString()
                makanan.waktuMakan = binding.mealType.text.toString()
                makanan.takaranSaji = binding.editTakaran.text.toString().toInt()
                makanan.jumlahKalori = binding.editJumlahKalori.text.toString().toInt()
                updateMakanan(makanan)
                dismiss()
            }
            setNeutralButton("Hapus"){
                    dialog, which ->
                deleteMakanan(makanan)
                dismiss()
            }
        }

        builder.setView(binding.root).setTitle("Daftar Makanan")
        return builder.create()
    }

    private fun deleteMakanan(makanan: MakananUser) {
        activity.executorService.execute{
            activity.mMakananDao.deleteMakanan(makanan)
        }
    }

    private fun updateMakanan(makanan: MakananUser) {
        activity.executorService.execute{
            activity.mMakananDao.updateMakanan(makanan)

        }
    }
}