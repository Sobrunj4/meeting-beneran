package com.meeting.tegal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.meeting.models.User
import com.example.meeting.utilities.Constants
import com.meeting.tegal.R
import com.meeting.tegal.viewmodels.UserState
import com.meeting.tegal.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var userViewModel : UserViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.listenToState().observer(this, Observer { handleUI(it) })
        doLogin()
        register.setOnClickListener { startActivity(Intent(this@LoginActivity, RegisterActivity::class.java)) }
    }

    private fun handleUI(it : UserState){
        when(it){
            is UserState.IsLoading -> btn_login.isEnabled = !it.state
            is UserState.ShowToast -> toast(it.message)
            is UserState.SuccessLogin -> {
                Constants.setToken(this@LoginActivity, "Bearer ${it.token}")
                startActivity(Intent(this@LoginActivity, MainActivity::class.java)).also { finish() }
            }
        }
    }

    private fun doLogin(){
        btn_login.setOnClickListener {
            val email = ed_email.text.toString().trim()
            val password = ed_password.text.toString().trim()
            userViewModel.login(email, password)
        }
    }

    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    override fun onResume() {
        super.onResume()
        if(Constants.getToken(this@LoginActivity) != "UNDEFINED"){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }
}
