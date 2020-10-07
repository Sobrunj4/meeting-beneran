package com.meeting.tegal.repository

import com.example.meeting.utilities.WrappedListResponse
import com.meeting.tegal.ApiService
import com.meeting.tegal.Partner
import com.meeting.tegal.utilities.ArrayResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface PartnerContract{
    fun fetchPartners(listener : ArrayResponse<Partner>)
}

class PartnerRepository (private val api : ApiService): PartnerContract{

    override fun fetchPartners(listener: ArrayResponse<Partner>) {
        api.fetchPartners().enqueue(object : Callback<WrappedListResponse<Partner>>{
            override fun onFailure(call: Call<WrappedListResponse<Partner>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedListResponse<Partner>>,
                response: Response<WrappedListResponse<Partner>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }
}