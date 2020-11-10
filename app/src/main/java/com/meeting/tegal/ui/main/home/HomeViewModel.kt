package com.meeting.tegal.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meeting.models.MeetingRoom
import com.example.meeting.utilities.SingleLiveEvent
import com.meeting.tegal.Partner
import com.meeting.tegal.repository.MeetingRepository
import com.meeting.tegal.repository.PartnerRepository
import com.meeting.tegal.utilities.ArrayResponse

class HomeViewModel (private val partnerRepository: PartnerRepository,
                     private val meetingRepository: MeetingRepository) : ViewModel(){
    private val state : SingleLiveEvent<HomeState> = SingleLiveEvent()
    private val partners = MutableLiveData<List<Partner>>()
    private val rooms = MutableLiveData<List<MeetingRoom>>()
    private fun isLoading(b : Boolean){ state.value = HomeState.Loading(b) }
    private fun toast(m : String){ state.value = HomeState.ShowToast(m) }

    fun fetchPartners(){
        partnerRepository.fetchPartners(object : ArrayResponse<Partner>{
            override fun onSuccess(datas: List<Partner>?) {
                isLoading(false)
                datas?.let { partners.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun fetchPromo() {
        isLoading(true)
        meetingRepository.fetchRoomsPromo(object : ArrayResponse<MeetingRoom>{
            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }

            override fun onSuccess(datas: List<MeetingRoom>?) {
                fetchPartners()
                datas?.let { rooms.postValue(it) }
            }
        })
    }

    fun listenToState() = state
    fun listenToPartners() = partners
    fun listenToPromoRooms() = rooms
}

sealed class HomeState{
    data class Loading(var state : Boolean = false) : HomeState()
    data class ShowToast(var message : String) : HomeState()
}