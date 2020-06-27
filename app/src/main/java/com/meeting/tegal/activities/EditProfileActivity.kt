package com.meeting.tegal.activities

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.meeting.tegal.R
import kotlinx.android.synthetic.main.activity_edit__profile_.*

class EditProfileActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit__profile_)
        btn_edit_profile.setOnClickListener { finish() }
    }
}
