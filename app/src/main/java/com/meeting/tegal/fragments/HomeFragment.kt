package com.meeting.tegal.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.meeting.tegal.R
import com.meeting.tegal.activities.MeetingActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        card_meeting.setOnClickListener {
            startActivity(Intent(activity!!, MeetingActivity::class.java))
        }
    }
}