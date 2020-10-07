package com.meeting.tegal.ui.maps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meeting.utilities.SingleLiveEvent
import com.meeting.tegal.Partner
import com.meeting.tegal.repository.PartnerRepository
import com.meeting.tegal.utilities.ArrayResponse

class MapsViewModel (private val partnerRepository: PartnerRepository) : ViewModel(){
    private val state : SingleLiveEvent<MapsState> = SingleLiveEvent()
    private val partners = MutableLiveData<List<Partner>>()

    private fun setLoading(){ state.value = MapsState.Loading(true) }
    private fun hideLoading(){ state.value = MapsState.Loading(false) }
    private fun toast(message: String){ state.value = MapsState.ShowToast(message) }

    fun fetchLatLngPartners(token : String){
        setLoading()
        partnerRepository.fetchPartners( object : ArrayResponse<Partner>{
            override fun onSuccess(datas: List<Partner>?) {
                hideLoading()
                partners.postValue(datas)
            }

            override fun onFailure(err: Error?) {
                hideLoading()
                toast(err?.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenPartners() = partners
}

sealed class MapsState{
    data class Loading(var state : Boolean = false) : MapsState()
    data class ShowToast(var message : String) : MapsState()
}