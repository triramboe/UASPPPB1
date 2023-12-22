import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calorieuas.AdminDialog
import com.example.calorieuas.MainActivity
import com.example.calorieuas.MakananUser
import com.example.calorieuas.R
import com.example.calorieuas.RiwayatDialog

class RiwayatAdapter(context: Context) : RecyclerView.Adapter<RiwayatAdapter.MyViewHolder>() {

    private var makananList = emptyList<MakananUser>()
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = makananList[position]
        holder.itemView.findViewById<TextView>(R.id.namaMakananUser).text = currentItem.namaMakanan
        holder.itemView.findViewById<TextView>(R.id.jmlKaloriUser).text = currentItem.jumlahKalori.toString()

        holder.itemView.findViewById<ImageButton>(R.id.btnEditMakananRiwayat).setOnClickListener {
            showEditMakananDialog(holder, currentItem)
        }
    }


    private fun showEditMakananDialog(holder: MyViewHolder, currentItem: MakananUser) {
        val fragmentManager = (holder.itemView.context as MainActivity).supportFragmentManager
        val riwayatDialog = RiwayatDialog(holder.itemView.context as MainActivity, currentItem)
        riwayatDialog.show(fragmentManager, "EditMakananDialog")
    }

    override fun getItemCount(): Int {
        // Return the size of your data set
        return makananList.size
    }

    fun setData(makananUser: List<MakananUser>){
        this.makananList = makananUser
        notifyDataSetChanged()

    }
}
