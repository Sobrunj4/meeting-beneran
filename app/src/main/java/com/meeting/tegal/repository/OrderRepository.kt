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

interface OrderContract{
    fun order(token: String, createOrder: CreateOrder, listener : SingleResponse<CreateOrder>)
    fun orderCancel(token: String, orderId : String, listener: SingleResponse<Order>)
}

class OrderRepository (private val api : ApiService) : OrderContract{

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

    override fun order(token: String, createOrder: CreateOrder, listener: SingleResponse<CreateOrder>) {
        val g = GsonBuilder().create()
        val json = g.toJson(createOrder)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.order(token, body).enqueue(object : Callback<WrappedResponse<CreateOrder>>{
            override fun onFailure(call: Call<WrappedResponse<CreateOrder>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<CreateOrder>>,
                response: Response<WrappedResponse<CreateOrder>>
            ) {
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if (b?.status!!) listener.onSuccess(b.data) else listener.onFailure(Error(b.message))
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun orderCancel(token: String, orderId: String, listener: SingleResponse<Order>) {
        api.orderCancel(token, orderId.toInt()).enqueue(object : Callback<WrappedResponse<Order>>{
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!) listener.onSuccess(body.data) else listener.onFailure(
                            Error(body.message)
                        )
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }
}