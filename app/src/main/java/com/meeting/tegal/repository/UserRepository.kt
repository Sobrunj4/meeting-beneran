package com.meeting.tegal.repository

import android.util.Log
import com.example.meeting.models.User
import com.example.meeting.utilities.WrappedResponse
import com.meeting.tegal.ApiService
import com.meeting.tegal.utilities.SingleResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface UserContract {
    fun profile(token: String, listener: SingleResponse<User>)
    fun login(email: String, password: String, listener: SingleResponse<User>)
}

class UserRepository (private val api : ApiService) : UserContract {
    private val TAG = "UserRep"

    override fun profile(token: String, listener: SingleResponse<User>) {
        api.profile(token).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) = listener.onFailure(Error(t.message))

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if(b?.status!!){
                            listener.onSuccess(b.data)
                        }else{
                            listener.onFailure(Error("Cannot get profile info"))
                        }
                    }
                    !response.isSuccessful ->{
                        listener.onFailure(Error(response.message()))
                    }
                }
            }
        })
    }

    override fun login(email: String, password: String, listener: SingleResponse<User>) {
        api.login(email, password).enqueue(object : Callback<WrappedResponse<User>> {
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) = listener.onFailure(Error(t.message.toString()))

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when{
                    response.isSuccessful -> {
                        response.body()?.let {
                            if(it.status!!){
                                listener.onSuccess(it.data)
                            }else{
                                listener.onFailure(Error(it.message.toString()))
                            }
                        }
                    }
                    !response.isSuccessful -> {
                        Log.d(TAG, response.message())
                        listener.onFailure(Error(response.message()))
                    }
                }
            }
        })
    }


}