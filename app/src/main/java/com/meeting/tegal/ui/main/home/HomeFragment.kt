package com.meeting.tegal.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.meeting.tegal.R
import com.meeting.tegal.ui.maps.MapsActivity
import com.meeting.tegal.ui.meeting.MeetingActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        card_meeting.setOnClickListener {
            startActivity(Intent(activity!!, MeetingActivity::class.java))
        }
        card_maps.setOnClickListener {
            startActivity(Intent(activity!!,MapsActivity::class.java))
        }
    }
}