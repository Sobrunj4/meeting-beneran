package com.meeting.tegal.repository

import com.example.meeting.utilities.WrappedResponse
import com.google.gson.GsonBuilder
import com.meeting.tegal.ApiService
import com.meeting.tegal.models.CreateOrder
import com.meeting.tegal.models.Order
import com.meeting.tegal.utilities.SingleResponse
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderRepository (private val api : ApiService) {
    fun order(token : String, createOrder: CreateOrder, result : (Boolean, Error?)->Unit){
        val g = GsonBuilder().create()
        val json = g.toJson(createOrder)
        println(json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.order(token, body).enqueue(object : Callback<WrappedResponse<CreateOrder>>{
            override fun onFailure(call: Call<WrappedResponse<CreateOrder>>, t: Throwable) {
                result(false, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<CreateOrder>>, response: Response<WrappedResponse<CreateOrder>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!) {
                        println(body.data)
                        println(body.message)
                        result(true, null)
                    }else{
                        result(false, Error(body.message))
                    }
                }else{
                    result(false, Error(response.message()))
                }
            }
        })
    }
}