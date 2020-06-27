package com.meeting.tegal.ui.available_room

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meeting.models.MeetingRoom
import com.example.meeting.utilities.SingleLiveEvent
import com.meeting.tegal.repository.MeetingRepository
import com.meeting.tegal.utilities.ArrayResponse

class AvailableRoomViewModel (private val meetingRepository: MeetingRepository) : ViewModel(){
    private val state : SingleLiveEvent<AvailableRoomState> = SingleLiveEvent()
    private val availableRooms = MutableLiveData<List<MeetingRoom>>()

    private fun setLoading(){
        state.value = AvailableRoomState.Loading(true)
    }

    private fun hideLoading(){
        state.value = AvailableRoomState.Loading(false)
    }

    private fun toast(message: String){
        state.value = AvailableRoomState.ShowToast(message)
    }

    fun searchMeetingRooms(token: String, dateAndTime: String, duration: String){
        setLoading()
        meetingRepository.searchForMeetingRooms(token, dateAndTime, duration, object :
            ArrayResponse<MeetingRoom> {
            override fun onSuccess(datas: List<MeetingRoom>?) {
                hideLoading()
                datas?.let { availableRooms.postValue(it) }
            }
            override fun onFailure(err: Error?) {
                hideLoading()
                err?.let { toast(it.message.toString()) }
            }
        })
    }


    fun listenToState()= state
    fun listenToRooms() = availableRooms
}

sealed class AvailableRoomState {
    data class Loading(val isLoading: Boolean) : AvailableRoomState()
    data class ShowToast(val message: String) : AvailableRoomState()
}