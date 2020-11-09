package com.meeting.tegal.repository

import android.util.Log
import com.example.meeting.models.User
import com.example.meeting.utilities.WrappedResponse
import com.meeting.tegal.ApiService
import com.meeting.tegal.utilities.SingleResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.HTTP

interface UserContract {
    fun profile(token: String, listener: SingleResponse<User>)
    fun login(email: String, password: String, listener: SingleResponse<User>)
    fun register(name : String, email: String, password: String, telp : String, uname : String, listener: SingleResponse<User>)
}

class UserRepository (private val api : ApiService) : UserContract {

    private val TAG = "UserRep"

    override fun register(name: String, email: String, password: String, telp: String, uname : String, listener: SingleResponse<User>) {
        api.register(name, email, password, telp, uname).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error("maaf, register gagal"))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

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
                        if(response.code() == 401){
                            listener.onFailure(Error("Login gagal"))
                        }else{
                            listener.onFailure(Error(response.message()))
                        }
                    }
                }
            }
        })
    }
}