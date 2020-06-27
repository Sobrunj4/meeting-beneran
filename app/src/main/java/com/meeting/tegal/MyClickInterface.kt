package com.meeting.tegal


import com.example.meeting.models.Makanan
import com.example.meeting.models.RuangMeeting

interface MyClickInterface {
    fun click(ruangMeeting: RuangMeeting)
}

interface MyClickListener {
    //fun clickPesanMakanan(makanan: Makanan)

    fun clickPesanMakanan(makanan: Makanan?, totalPesan: Int)
}