package com.meeting.tegal.ui.detail_harga

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meeting.models.Food
import com.example.meeting.utilities.Constants
import com.meeting.tegal.R
import kotlinx.android.synthetic.main.item_detail_harga.view.*

class DetailHargaAdapter (private var foods : MutableList<Food>, private var context: Context)
    : RecyclerView.Adapter<DetailHargaAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_detail_harga, parent, false))
    }

    override fun getItemCount(): Int =foods.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(foods[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(food: Food, context: Context){
            with(itemView){
                label_item_makanan.text = food.name
                txt_qty_makanan.text = "x ${food.qty}"
                txt_item_makanan.text = Constants.setToIDR(food.price!!.times(food.qty!!))
            }
        }
    }

    fun changelist(c : List<Food>){
        foods.clear()
        foods.addAll(c)
        notifyDataSetChanged()
    }

}