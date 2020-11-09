package com.meeting.tegal.ui.register

import androidx.lifecycle.ViewModel
import com.example.meeting.models.User
import com.example.meeting.utilities.Constants
import com.example.meeting.utilities.SingleLiveEvent
import com.meeting.tegal.repository.UserRepository
import com.meeting.tegal.utilities.SingleResponse

class RegisterViewModel (private val userRepository: UserRepository) : ViewModel() {
    private val state : SingleLiveEvent<RegisterState> = SingleLiveEvent()

    private fun setLoading() { state.value = RegisterState.IsLoading(true) }
    private fun hideLoading() { state.value = RegisterState.IsLoading(false) }
    private fun toast(message: String){ state.value = RegisterState.ShowToast(message) }
    private fun success(email: String) { state.value = RegisterState.Success(email) }
    private fun reset() { state.value = RegisterState.Reset }

    fun validate(name: String, email: String, password: String, confirmPassowrd: String, telp: String) : Boolean {
        reset()
        if (name.isEmpty()){
            state.value = RegisterState.Validate(name = "nama tidak boleh kosong")
            return false
        }
        if (name.length < 5){
            state.value = RegisterState.Validate(name = "nama setidaknya 5 karakter")
            return false
        }

        if (email.isEmpty()){
            state.value = RegisterState.Validate(email = "email tidak boleh kosong")
            return false
        }
        if (!Constants.isValidEmail(email)){
            state.value = RegisterState.Validate(email = "email tidak valid")
            return false
        }

        if(password.isEmpty()){
            state.value = RegisterState.Validate(password = "password tidak boleh kosong")
            return false
        }

        if (!Constants.isValidPassword(password)){
            state.value = RegisterState.Validate(password = "password minimal 8 karakter")
            return false
        }
        if (confirmPassowrd.isEmpty()){
            state.value = RegisterState.Validate(confirmPassowrd = "konfirmasi password tidak boleh kosong")
            return false
        }
        if(confirmPassowrd != password){
            state.value = RegisterState.Validate(confirmPassowrd = "konfirmasi password tidak cocok")
            return false
        }
        if (telp.isEmpty()){
            state.value = RegisterState.Validate( telp = "no telepon tidak boleh kosong")
            return false
        }
        if (telp.length < 10 || telp.length > 13){
            state.value = RegisterState.Validate(telp = "no telepon setidaknya 11 sampai 13 karakter")
            return false
        }
        return true
    }

    fun register(name : String, email: String, password: String, telp: String, uname : String){
        setLoading()
        userRepository.register(name, email, password, telp, uname, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                hideLoading()
                data?.let { success(it.email!!) }
            }

            override fun onFailure(err: Error?) {
                hideLoading()
                toast(err?.message.toString())
            }

        })
    }


    fun listenToState() = state
}

sealed class RegisterState{
    data class IsLoading(var state : Boolean = false) : RegisterState()
    data class ShowToast(var message : String) : RegisterState()
    data class Success(var email : String) : RegisterState()
    data class Validate(
        var name : String? = null,
        var email : String? = null,
        var password : String? = null,
        var confirmPassowrd : String? = null,
        var telp : String? = null
    ) : RegisterState()
    object Reset : RegisterState()
}