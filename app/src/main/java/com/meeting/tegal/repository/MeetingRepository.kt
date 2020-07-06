package com.meeting.tegal.repository

import com.example.meeting.models.MeetingRoom
import com.example.meeting.utilities.WrappedListResponse
import com.meeting.tegal.ApiService
import com.meeting.tegal.utilities.ArrayResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface MeetingContract {
    fun getMeetingRooms(token: String, listener: ArrayResponse<MeetingRoom>)
    fun searchForMeetingRooms(token: String, dateAndTime: String, duration: String, listener: ArrayResponse<MeetingRoom>)
}

class MeetingRepository (private val api: ApiService) : MeetingContract {
    override fun getMeetingRooms(token: String, listener: ArrayResponse<MeetingRoom>) {
        api.getMeetingRooms(token).enqueue(object : Callback<WrappedListResponse<MeetingRoom>> {
            override fun onFailure(call: Call<WrappedListResponse<MeetingRoom>>, t: Throwable) {
                println(t.message)
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<MeetingRoom>>, response: Response<WrappedListResponse<MeetingRoom>>) {
                when{
                    response.isSuccessful -> {
                        response.body()?.let {
                            if(it.status!!) listener.onSuccess(it.data) else listener.onFailure(null)
                        }
                    }
                    !response.isSuccessful -> {
                        listener.onFailure(Error(response.message()))
                    }
                }
            }
        })
    }

    override fun searchForMeetingRooms(token: String, dateAndTime: String, duration: String, listener: ArrayResponse<MeetingRoom>) {
        api.search(token, dateAndTime, duration).enqueue(object: Callback<WrappedListResponse<MeetingRoom>>{
            override fun onFailure(call: Call<WrappedListResponse<MeetingRoom>>, t: Throwable) = listener.onFailure(Error(t.message))

            override fun onResponse(call: Call<WrappedListResponse<MeetingRoom>>, response: Response<WrappedListResponse<MeetingRoom>>) {
                when{
                    response.isSuccessful -> {
                        if(response.body()?.status!!){
                            listener.onSuccess(response.body()?.data)
                        }else{
                            listener.onFailure(Error("No data"))
                        }
                    }
                    !response.isSuccessful -> {
                        listener.onFailure(Error(response.message()))
                    }
                }
            }
        })
    }
}