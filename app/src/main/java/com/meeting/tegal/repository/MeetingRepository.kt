package com.meeting.tegal.repository

import com.example.meeting.models.MeetingRoom
import com.example.meeting.utilities.WrappedListResponse
import com.meeting.tegal.ApiService
import com.meeting.tegal.utilities.ArrayResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface MeetingContract {
    //fun getMeetingRooms(token: String, listener: ArrayResponse<MeetingRoom>)
    fun searchForMeetingRooms(token: String, dateAndTime: String, duration: String, listener: ArrayResponse<MeetingRoom>)
    fun fetchRoomsPromo(listener: ArrayResponse<MeetingRoom>)
    fun fetchRoomsByPartner(idPartner : String, listener: ArrayResponse<MeetingRoom>)
}

class MeetingRepository (private val api: ApiService) : MeetingContract {

    override fun searchForMeetingRooms(token: String, dateAndTime: String, duration: String, listener: ArrayResponse<MeetingRoom>) {
        api.search(token, dateAndTime, duration).enqueue(object: Callback<WrappedListResponse<MeetingRoom>>{
            override fun onFailure(call: Call<WrappedListResponse<MeetingRoom>>, t: Throwable) = listener.onFailure(Error(t.message))

            override fun onResponse(call: Call<WrappedListResponse<MeetingRoom>>, response: Response<WrappedListResponse<MeetingRoom>>) {
                when{
                    response.isSuccessful -> {
                        if(response.body()?.status!!){
                            listener.onSuccess(response.body()?.data)
                        }else{
                            listener.onFailure(Error(response.body()?.message))
                        }
                    }
                    !response.isSuccessful -> {
                        listener.onFailure(Error(response.message()))
                    }
                }
            }
        })
    }

    override fun fetchRoomsPromo(listener: ArrayResponse<MeetingRoom>) {
        api.fetchRoomsPromo().enqueue(object : Callback<WrappedListResponse<MeetingRoom>>{
            override fun onFailure(call: Call<WrappedListResponse<MeetingRoom>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedListResponse<MeetingRoom>>,
                response: Response<WrappedListResponse<MeetingRoom>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun fetchRoomsByPartner(idPartner: String, listener: ArrayResponse<MeetingRoom>) {
        api.fetchRoomsByPartner(idPartner.toInt()).enqueue(object : Callback<WrappedListResponse<MeetingRoom>>{
            override fun onFailure(call: Call<WrappedListResponse<MeetingRoom>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedListResponse<MeetingRoom>>,
                response: Response<WrappedListResponse<MeetingRoom>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }
}