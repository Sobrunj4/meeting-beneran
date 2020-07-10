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
            state.value = RegisterState.ShowToast("nama tidak boleh kosong")
            return false
        }
        if (name.length < 5){
            state.value = RegisterState.ShowToast("nama setidaknya 5 karakter")
            return false
        }
        if (telp.isEmpty()){
            state.value = RegisterState.ShowToast("no telepon harus di isi")
        }
        if (telp.length in 14 downTo 10){
            state.value = RegisterState.ShowToast("no telepon setidaknya 11 sampai 13 karakter")
        }

        if (email.isEmpty() || password.isEmpty()){
            state.value = RegisterState.ShowToast("mohon isi semua form")
            return false
        }
        if (!Constants.isValidEmail(email)){
            state.value = RegisterState.Validate(email = "email tidak valid")
            return false
        }
        if (!Constants.isValidPassword(password)){
            state.value = RegisterState.Validate(password = "password tidak valid")
            return false
        }
        if (confirmPassowrd.isEmpty()){
            state.value = RegisterState.ShowToast("Isi semua form terlebih dahulu")
            return false
        }
        if(!confirmPassowrd.equals(password)){
            state.value = RegisterState.Validate(confirmPassowrd = "Konfirmasi password tidak cocok")
            return false
        }
        return true
    }

    fun register(name : String, email: String, password: String, telp: String){
        setLoading()
        userRepository.register(name, email, password, telp, object : SingleResponse<User>{
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