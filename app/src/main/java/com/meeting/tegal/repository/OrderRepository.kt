package com.meeting.tegal.repository

import com.example.meeting.utilities.WrappedListResponse
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

    fun getOrderByUser(token: String, result: (List<Order>?, Error?) -> Unit){
        api.getOrderByUser(token).enqueue(object : Callback<WrappedListResponse<Order>>{
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) {
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Order>>, response: Response<WrappedListResponse<Order>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        result(data, null)
                    }else{
                        result(null, Error())
                    }
                }else{
                    result(null, Error(response.message()))
                }
            }
        })
    }

    fun updateStatus(token : String, id : String, status : String, result: (Boolean, Error?) -> Unit){
        api.updateStatus(token, id.toInt(), status).enqueue(object : Callback<WrappedResponse<Order>>{
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                result(false, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
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