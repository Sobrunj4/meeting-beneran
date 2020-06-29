package com.meeting.tegal.repository

import com.example.meeting.models.Food
import com.example.meeting.utilities.WrappedListResponse
import com.meeting.tegal.ApiService
import com.meeting.tegal.utilities.ArrayResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface FoodContract{
    fun getFoods(token: String, partnerId : String, listener: ArrayResponse<Food>)
}

class FoodRepository (private val api : ApiService) : FoodContract {
    override fun getFoods(token: String, partnerId: String, listener: ArrayResponse<Food>) {
        api.getMakanan(token, partnerId.toInt()).enqueue(object : Callback<WrappedListResponse<Food>>{
            override fun onFailure(call: Call<WrappedListResponse<Food>>, t: Throwable) = listener.onFailure(Error(t.message))

            override fun onResponse(call: Call<WrappedListResponse<Food>>, response: Response<WrappedListResponse<Food>>) {
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if(b?.status!!){
                            listener.onSuccess(b.data)
                        }else{
                            listener.onFailure(null)
                        }
                    }
                    !response.isSuccessful ->listener.onFailure(Error(response.message()))
                }
            }
        })
    }

}