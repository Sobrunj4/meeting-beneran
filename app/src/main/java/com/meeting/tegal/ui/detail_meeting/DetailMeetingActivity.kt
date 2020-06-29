package com.meeting.tegal.ui.detail_meeting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.api.load
import com.example.meeting.models.MeetingRoom
import com.meeting.tegal.R
import com.meeting.tegal.ui.order.OrderActivity
import kotlinx.android.synthetic.main.activity_detail_meeting.*

class DetailMeetingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_meeting)

        tv_nama_ruangan.text = getPassedMeeting()?.nama_tempat
        tv_harga1.text = getPassedMeeting()?.harga_sewa.toString()
        tv_keterangan.text = getPassedMeeting()?.keterangan
        //tv_fasilitas1.text = getPassedMeeting()?.mitra!!.makanans.joinToString { makanan -> makanan.nama!!  }
        iv_ruangan.load("https://meeting-ning-tegal.herokuapp.com/uploads/ruangmeeting/"+getPassedMeeting()?.foto)
        btn_pesan_sekarang.setOnClickListener { startActivity(Intent(this@DetailMeetingActivity, OrderActivity::class.java).apply {
            putExtra("RUANGAN", getPassedMeeting())
            putExtra("TANGGAL_DAN_WAKTU", getPassedTanggalDanWaktu())
            putExtra("LAMA", getPassedLama())
        }) }
    }
    private fun getPassedMeeting() : MeetingRoom?  = intent.getParcelableExtra("MEETING")
    private fun getPassedTanggalDanWaktu() = intent.getStringExtra("TANGGAL_DAN_WAKTU")
    private fun getPassedLama() = intent.getStringExtra("LAMA")
}
