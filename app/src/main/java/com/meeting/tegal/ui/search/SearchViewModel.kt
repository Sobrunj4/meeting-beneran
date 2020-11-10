package com.meeting.tegal.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meeting.utilities.SingleLiveEvent
import com.meeting.tegal.Partner
import com.meeting.tegal.repository.PartnerRepository
import com.meeting.tegal.utilities.ArrayResponse

class SearchViewModel (private var partnerRepository: PartnerRepository) : ViewModel(){
    private val state : SingleLiveEvent<SearchState> = SingleLiveEvent()
    private val partners = MutableLiveData<List<Partner>>()

    private fun isLoading(b : Boolean){ state.value =  SearchState.Loading(b) }
    private fun toast(m : String){ state.value = SearchState.ShowToast(m) }

    fun searchPartners(date : String, startTime : String, endTime : String){
        isLoading(true)
        partnerRepository.searchPartners(date, startTime, endTime, object : ArrayResponse<Partner>{
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

    fun listenToState() = state
    fun listenToPartners() = partners
}

sealed class SearchState{
    data class Loading(var state : Boolean) : SearchState()
    data class ShowToast(var message : String) : SearchState()
}