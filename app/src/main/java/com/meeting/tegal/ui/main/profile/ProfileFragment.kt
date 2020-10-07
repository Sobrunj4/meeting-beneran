package com.meeting.tegal.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.meeting.utilities.Constants
import com.meeting.tegal.R
import com.meeting.tegal.ui.edit_profile.EditProfileActivity
import com.meeting.tegal.ui.login.LoginActivity
import com.meeting.tegal.ui.password.PasswordActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(R.layout.fragment_profile){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        profile.setOnClickListener {
//            startActivity(Intent(activity, EditProfileActivity::class.java))
//        }
//
//        Password.setOnClickListener {
//            startActivity(Intent(activity, PasswordActivity::class.java))
//        }
        btn_logout.setOnClickListener {
            Constants.clearToken(activity!!)
        startActivity(Intent(activity!!, LoginActivity::class.java))
        activity!!.finish()
    }

    }
}