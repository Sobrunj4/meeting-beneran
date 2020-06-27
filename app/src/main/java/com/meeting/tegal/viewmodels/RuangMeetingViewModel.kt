package com.meeting.tegal.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meeting.models.RuangMeeting
import com.example.meeting.utilities.SingleLiveEvent
import com.example.meeting.utilities.WrappedListResponse
import com.meeting.tegal.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RuangMeetingViewModel : ViewModel(){
    private var ruangMeetings = MutableLiveData<List<RuangMeeting>>()
    private var state : SingleLiveEvent<RuangMeetingState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    private fun setLoading() { state.value = RuangMeetingState.IsLoading(true) }
    private fun hideLoading() { state.value = RuangMeetingState.IsLoading(false) }
    private fun toast(message: String) { state.value = RuangMeetingState.ShowToast(message) }

    fun getALlRuangMeeting(token : String){
        setLoading()
        api.getRuangMeeting(token).enqueue(object : Callback<WrappedListResponse<RuangMeeting>>{
            override fun onFailure(call: Call<WrappedListResponse<RuangMeeting>>, t: Throwable) {
                println("OnFailure : "+t.message)
                hideLoading()
            }

            override fun onResponse(call: Call<WrappedListResponse<RuangMeeting>>, response: Response<WrappedListResponse<RuangMeeting>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        ruangMeetings.postValue(data)
                    }else{
                        state.value = RuangMeetingState.ShowToast("Tidak dapat mengambil data")
                    }
                }else{
                    state.value = RuangMeetingState.ShowToast("Tidak dapat mengambil ")
                }
                hideLoading()
            }

        })
    }

    fun search(token: String, tanggalDanWaktu : String, lama : String){
        setLoading()
        api.search(token, tanggalDanWaktu, lama).enqueue(object : Callback<WrappedListResponse<RuangMeeting>>{
            override fun onFailure(call: Call<WrappedListResponse<RuangMeeting>>, t: Throwable) {
                println(t.message)
                hideLoading()
            }

            override fun onResponse(call: Call<WrappedListResponse<RuangMeeting>>, response: Response<WrappedListResponse<RuangMeeting>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data

                        ruangMeetings.postValue(data)
                    }else{
                        println("status false ${body.message}")
                    }
                }else{
                    println("not response ${response.message()}")
                }
                hideLoading()
            }

        })
    }

    fun getState() = state
    fun getRuangMeetings() = ruangMeetings
}

sealed class RuangMeetingState{
    data class ShowToast(var message : String) : RuangMeetingState()
    data class IsLoading(var state : Boolean) : RuangMeetingState()
}