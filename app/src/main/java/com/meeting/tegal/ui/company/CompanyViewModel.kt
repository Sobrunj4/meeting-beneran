package com.meeting.tegal.ui.company

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meeting.models.MeetingRoom
import com.example.meeting.utilities.SingleLiveEvent
import com.meeting.tegal.Partner
import com.meeting.tegal.repository.MeetingRepository
import com.meeting.tegal.utilities.ArrayResponse

class CompanyViewModel(private val meetingRepository: MeetingRepository)  : ViewModel(){

    private val state : SingleLiveEvent<CompanyState> = SingleLiveEvent()
    private val rooms = MutableLiveData<List<MeetingRoom>>()
    private fun isLoading(b : Boolean){ state.value = CompanyState.Loading(b) }
    private fun toast(m : String){ state.value = CompanyState.ShowToast(m) }

    fun fetchRoomsByPartner(id : String){
        isLoading(true)
        meetingRepository.fetchRoomsByPartner(id, object : ArrayResponse<MeetingRoom>{
            override fun onSuccess(datas: List<MeetingRoom>?) {
                isLoading(false)
                datas?.let { rooms.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun listenToState() = state
    fun listenToRooms() = rooms
}

sealed class CompanyState{
    data class Loading(var state : Boolean = false) : CompanyState()
    data class ShowToast(var message : String) : CompanyState()
}