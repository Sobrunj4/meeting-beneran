package com.meeting.tegal.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.meeting.utilities.Constants
import com.meeting.tegal.R
import com.meeting.tegal.ui.main.MainActivity
import com.meeting.tegal.ui.register.RegisterActivity
import com.meeting.tegal.utilities.toast
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        observe()
        doLogin()
        register.setOnClickListener { startActivity(Intent(this@LoginActivity, RegisterActivity::class.java)) }
    }

    private fun observe() = observeState()

    private fun observeState() = loginViewModel.listenToState().observer(this, Observer { handleUI(it) })

    private fun handleUI(it : LoginState){
        when(it){
            is LoginState.Loading -> isLoading(it.isLoading)
            is LoginState.ShowToast -> toast(it.message)
            is LoginState.Success -> handleSuccess(it.token)
            is LoginState.Validate -> {
                it.email?.let { setEmailError(it) }
                it.password?.let { setPasswordError(it) }
            }
            is LoginState.Reset -> {
                setEmailError(null)
                setPasswordError(null)
            }
        }
    }

    private fun handleSuccess(token: String){
        Constants.setToken(this@LoginActivity, "Bearer $token")
        goToMainActivity()
    }

    private fun goToMainActivity() = startActivity(Intent(this@LoginActivity, MainActivity::class.java)).also { finish() }


    private fun isLoading(b: Boolean){
        btn_login.isEnabled = !b
    }

    private fun doLogin(){
        btn_login.setOnClickListener {
            val email = ed_email.text.toString().trim()
            val password = ed_password.text.toString().trim()
            if (loginViewModel.validate(email, password)){
                loginViewModel.login(email, password)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(Constants.getToken(this@LoginActivity) != "UNDEFINED"){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun setEmailError(err : String?) { til_email.error = err }
    private fun setPasswordError(err : String?) { til_password.error = err }
}
