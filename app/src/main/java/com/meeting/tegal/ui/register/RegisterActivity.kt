package com.meeting.tegal.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Observer
import com.meeting.tegal.R
import com.meeting.tegal.ui.login.LoginActivity
import com.meeting.tegal.utilities.disable
import com.meeting.tegal.utilities.enable
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val registerViewModel : RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerViewModel.listenToState().observer(this, Observer { handleUI(it) })
        register()
    }

    private fun handleUI(it: RegisterState) {
        when(it){
            is RegisterState.ShowToast -> toast(it.message)
            is RegisterState.IsLoading -> {
                if (it.state){
                    btn_register.enable()
                }else{
                    btn_register.disable()
                }
            }
            is RegisterState.Reset -> {
                setNameError(null)
                setEmailError(null)
                setPasswordError(null)
                setConfirmPasswordError(null)
                setTelpError(null)
            }
            is RegisterState.Validate -> {
                it.name?.let { setNameError(it) }
                it.email?.let { setEmailError(it) }
                it.password?.let { setPasswordError(it) }
                it.confirmPassowrd?.let { setConfirmPasswordError(it) }
                it.telp?.let { setTelpError(it) }
            }
            is RegisterState.Success -> success(it.email)
        }
    }

    private fun register() {
        btn_register.setOnClickListener {
            val name = et_name.text.toString().trim()
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            val cPassowrd = et_confirm_password.text.toString().trim()
            val telp = et_telp.text.toString().trim()
            val uname = et_uname.text.toString().trim()

            if (registerViewModel.validate(name, email, password, cPassowrd, telp)){
                registerViewModel.register(name, email, password, telp, uname)
            }
        }
    }


    private fun setNameError(err : String?){ til_name.error = err }
    private fun setEmailError(err : String?){ til_email.error = err }
    private fun setPasswordError(err : String?){ til_password.error = err }
    private fun setConfirmPasswordError(err : String?){ til_confirm_password.error = err }
    private fun setTelpError(err : String?){ til_telp.error = err }
    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    private fun success(email : String) {
        AlertDialog.Builder(ContextThemeWrapper(this, R.style.ThemeOverlay_AppCompat_Dialog_Alert)).apply {
            setMessage("Kami telah mengirim email ke $email. Pastikan anda telah memverifikasi email sebelum login")
            setPositiveButton("Mengerti"){ d, _ ->
                d.cancel()
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                this@RegisterActivity.finish()
            }.create().show()
        }
    }
}
