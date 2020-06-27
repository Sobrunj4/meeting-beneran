package com.meeting.tegal.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meeting.models.User
import com.example.meeting.utilities.SingleLiveEvent
import com.example.meeting.utilities.WrappedResponse
import com.meeting.tegal.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel(){
    private var  state : SingleLiveEvent<UserState> = SingleLiveEvent()
    private var user = MutableLiveData<User>()
    private var api = ApiClient.instance()

    private fun toast(message: String) { state.value = UserState.ShowToast(message) }
    private fun setLoading() { state.value = UserState.IsLoading(true) }
    private fun hideLoading() { state.value = UserState.IsLoading(false) }

    fun login(email : String, pass : String){
        setLoading()
        api.login(email, pass).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println(t.message)
                hideLoading()
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    body?.let {
                        state.value = UserState.SuccessLogin(it.data!!.token!!)
                    }
                }
                hideLoading()
            }

        })
    }

    fun profile(token: String){
        setLoading()
        api.profile(token).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println(t.message)
                hideLoading()
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    body?.let {
                        user.postValue(it.data)
                    }
                }
                hideLoading()
            }

        })
    }

    fun listenToState() = state
    fun listenToUser() = user

}

sealed class UserState{
    data class IsLoading(var state : Boolean = false) : UserState()
    data class ShowToast(var message : String) : UserState()
    data class SuccessLogin(var token : String)  : UserState()
}