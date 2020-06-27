package com.meeting.tegal.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.meeting.tegal.R
import com.meeting.tegal.activities.EditProfileActivity
import com.meeting.tegal.activities.PasswordActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(R.layout.fragment_profile){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        profile.setOnClickListener {
            startActivity(Intent(activity, EditProfileActivity::class.java))
        }

        Password.setOnClickListener {
            startActivity(Intent(activity, PasswordActivity::class.java))
        }

    }
}