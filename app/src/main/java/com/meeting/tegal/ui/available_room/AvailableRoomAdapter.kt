package com.meeting.tegal.ui.available_room

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.meeting.models.MeetingRoom
import com.example.meeting.utilities.Constants
import com.meeting.tegal.R
import kotlinx.android.synthetic.main.item_meeting.view.*

class AvailableRoomAdapter (private val rooms: MutableList<MeetingRoom>, private val availableRoomInterface: AvailableRoomInterface) : RecyclerView.Adapter<AvailableRoomAdapter.ViewHolder>(){

    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview){
        @SuppressLint("SetTextI18n")
        fun bind(room: MeetingRoom){
            with(itemView){
                //tv_nama_mitra.text = room.partner.nama_mitra
                tv_nama_ruangan.text = room.nama_tempat
                tv_kapasitas.text = "${room.kapasitas} Orang"
                tv_harga.text = "${Constants.setToIDR(room.harga_sewa!!)}/Jam"
                iv_ruangan.load("https://meeting-ning-tegal.herokuapp.com/uploads/ruangmeeting/"+room.foto)
                setOnClickListener {
                    availableRoomInterface.click(room)
                }
            }
        }
    }

    fun changeList(it: List<MeetingRoom>){
        rooms.clear()
        rooms.addAll(it)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_meeting, parent, false))
    }

    override fun getItemCount(): Int = rooms.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(rooms[position])
}