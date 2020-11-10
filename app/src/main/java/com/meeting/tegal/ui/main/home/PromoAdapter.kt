package com.meeting.tegal.ui.main.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meeting.models.MeetingRoom
import com.example.meeting.utilities.Constants
import com.meeting.tegal.R
import kotlinx.android.synthetic.main.item_promo.view.*

class PromoAdapter (private var rooms : MutableList<MeetingRoom>, private var context: Context)
    : RecyclerView.Adapter<PromoAdapter.ViewHolder>(){

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("WrongViewCast")
        fun bind(room: MeetingRoom, context: Context){
            with(itemView){
                //val prtice = findViewById<TextView>(R.id.txt_price) as RichTextView
                txt_price_promo.text = Constants.setToIDR(room.promo?.promo_price!!)
                txt_room_name.text = room.nama_tempat
                txt_price.text = Constants.setToIDR(room.harga_sewa!!)
                //price.formatSpan(78, 92, RichTextView.FormatType.STRIKETHROUGH)
            }
        }
    }

    fun changeList(c : List<MeetingRoom>){
        rooms.clear()
        rooms.addAll(c)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_promo, parent, false))
    }

    override fun getItemCount(): Int = rooms.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(rooms[position], context)
}