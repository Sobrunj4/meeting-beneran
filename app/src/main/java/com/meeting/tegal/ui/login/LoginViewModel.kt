package com.meeting.tegal.ui.login

import androidx.lifecycle.ViewModel
import com.example.meeting.models.User
import com.example.meeting.utilities.Constants
import com.example.meeting.utilities.SingleLiveEvent
import com.meeting.tegal.repository.UserRepository
import com.meeting.tegal.utilities.SingleResponse

class LoginViewModel (private val userRepository: UserRepository): ViewModel(){
    private val state: SingleLiveEvent<LoginState> = SingleLiveEvent()

    private fun setLoading(){ state.value = LoginState.Loading(true) }
    private fun hideLoading(){ state.value = LoginState.Loading(false) }
    private fun toast(message: String){ state.value = LoginState.ShowToast(message) }
    private fun success(token: String){ state.value = LoginState.Success(token) }

    fun login(email: String, password: String){
        setLoading()
        userRepository.login(email, password, object: SingleResponse<User> {
            override fun onSuccess(data: User?) {
                hideLoading()
                data?.let { success(it.token.toString()) }
            }
            override fun onFailure(err: Error?) {
                hideLoading()
                toast(err?.message.toString())
            }
        })
    }

    fun validate(email : String, password: String) : Boolean {
        state.value = LoginState.Reset
        if (email.isEmpty()){
            state.value = LoginState.Validate(email = "email tidak boleh kosong")
            return false
        }
        if (!Constants.isValidEmail(email)){
            state.value = LoginState.Validate(email = "email tidak valid")
            return false
        }

        if (password.isEmpty()){
            state.value = LoginState.Validate(password = "password tidak boleh kosong")
            return false
        }

        if (!Constants.isValidPassword(password)){
            state.value = LoginState.Validate(password = "password minimal 8 karakter")
            return false
        }
         return true
    }

    fun listenToState() = state
}

sealed class LoginState {
    data class ShowToast(val message: String) : LoginState()
    data class Loading(val isLoading: Boolean) : LoginState()
    data class Success(val token: String) : LoginState()
    object Reset : LoginState()
    data class Validate(
        var email: String? = null,
        var password: String? = null
    ) : LoginState()
}