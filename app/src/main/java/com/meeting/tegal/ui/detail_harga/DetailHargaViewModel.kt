package com.meeting.tegal.ui.detail_harga

import androidx.lifecycle.ViewModel
import com.example.meeting.utilities.SingleLiveEvent
import com.meeting.tegal.models.CreateOrder
import com.meeting.tegal.repository.OrderRepository
import com.meeting.tegal.utilities.SingleResponse

class DetailHargaViewModel (private val orderRepository: OrderRepository) : ViewModel(){
    private val state : SingleLiveEvent<DetailHargaState> = SingleLiveEvent()
    private fun setLoading() { state.value = DetailHargaState.IsLoading(true) }
    private fun hideLoading() { state.value = DetailHargaState.IsLoading(false) }
    private fun toast(message: String) { state.value = DetailHargaState.ShowToast(message) }
    private fun success(){ state.value = DetailHargaState.Success }

    fun order(token: String, createOrder : CreateOrder) {
        setLoading()
        orderRepository.order(token, createOrder, object : SingleResponse<CreateOrder> {
            override fun onSuccess(data: CreateOrder?) {
                hideLoading()
                data?.let { success() }
            }

            override fun onFailure(err: Error?) {
                hideLoading()
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun listenToState() = state
}

sealed class DetailHargaState{
    data class IsLoading(var state : Boolean = false) : DetailHargaState()
    data class ShowToast(var message : String) : DetailHargaState()
    object Success : DetailHargaState()
}