package com.meeting.tegal.ui.company

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.meeting.models.MeetingRoom
import com.meeting.tegal.R
import com.meeting.tegal.ui.detail_meeting.DetailMeetingActivity
import com.meeting.tegal.utilities.toast
import kotlinx.android.synthetic.main.item_room.view.*

class CompanyAdapter (private var rooms : MutableList<MeetingRoom>, private var companyClickListener: CompanyClickListener)
    : RecyclerView.Adapter<CompanyAdapter.ViewHolder>(){

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(room: MeetingRoom){
            with(itemView){
                txt_room_name.text = room.nama_tempat
                txt_room_capacity.text = "Kapasitas : ${room.kapasitas}"
                txt_room_price.text = "Harga : ${room.harga_sewa}"
                img_room.load(room.foto)
                setOnClickListener { companyClickListener.click(room) }
            }
        }
    }

    fun changeList(c : List<MeetingRoom>){
        rooms.clear()
        rooms.addAll(c)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false))
    }

    override fun getItemCount(): Int = rooms.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(rooms[position])
}