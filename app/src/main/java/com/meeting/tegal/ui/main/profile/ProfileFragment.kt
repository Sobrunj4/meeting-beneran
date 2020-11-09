package com.meeting.tegal.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
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

        if (!isLoggedIn()){
            alertNotLogin("silahkan login dahulu")
        }

        logout()
    }

    private fun isLoggedIn() = !Constants.getToken(activity!!).equals("UNDEFINED")

    private fun logout(){
        btn_logout.setOnClickListener {
            Constants.clearToken(activity!!)
            startActivity(Intent(activity!!, LoginActivity::class.java))
            activity!!.finish()
        }
    }

    fun alertNotLogin(message: String){
        AlertDialog.Builder(requireActivity()).apply {
            setMessage(message)
            setPositiveButton("ya"){dialogInterface, _ ->
                startActivity(Intent(requireActivity(), LoginActivity::class.java)
                    .putExtra("EXPECT_RESULT", true))
                dialogInterface.dismiss()
            }
        }.show()
    }
}