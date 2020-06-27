package com.meeting.tegal.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.meeting.models.RuangMeeting
import com.example.meeting.utilities.Constants
import com.meeting.tegal.MyClickInterface
import com.meeting.tegal.R
import com.meeting.tegal.activities.DetailMeetingActivity
import kotlinx.android.synthetic.main.activity_detail_meeting.view.*
import kotlinx.android.synthetic.main.item_meeting.view.*
import kotlinx.android.synthetic.main.item_meeting.view.iv_ruangan
import kotlinx.android.synthetic.main.item_meeting.view.tv_harga
import kotlinx.android.synthetic.main.item_meeting.view.tv_nama_ruangan

class MeetingAdapter (private var ruangMeeting: MutableList<RuangMeeting>, private var myClickInterface: MyClickInterface)
    : RecyclerView.Adapter<MeetingAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_meeting, parent, false))
    }

    override fun getItemCount(): Int = ruangMeeting.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(ruangMeeting[position], myClickInterface)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(ruangMeeting: RuangMeeting, myClickInterface: MyClickInterface){
            with(itemView){
                tv_nama_mitra.text = ruangMeeting.mitra.nama_mitra
                tv_nama_ruangan.text = ruangMeeting.nama_tempat
                tv_kapasitas.text = "${ruangMeeting.kapasitas} Orang"
                tv_harga.text = "${Constants.setToIDR(ruangMeeting.harga_sewa!!)}/Jam"
                iv_ruangan.load("https://meeting-ning-tegal.herokuapp.com/uploads/ruangmeeting/"+ruangMeeting.foto)
                setOnClickListener {
                    myClickInterface.click(ruangMeeting)
                }
                /*setOnClickListener {
                    context.startActivity(Intent(context, DetailMeetingActivity::class.java).apply {
                        putExtra("MEETING", ruangMeeting)
                    })
                    Toast.makeText(context, ruangMeeting.mitra.nama_mitra, Toast.LENGTH_SHORT).show()
                }*/

            }
        }
    }

    fun changelist(c : List<RuangMeeting>){
        ruangMeeting.clear()
        ruangMeeting.addAll(c)
        notifyDataSetChanged()
    }
}