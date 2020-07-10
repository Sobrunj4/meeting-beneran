package com.meeting.tegal.ui.main.shopping

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meeting.utilities.SingleLiveEvent
import com.meeting.tegal.models.Order
import com.meeting.tegal.repository.OrderRepository

class ShoppingViewModel (private val orderRepository: OrderRepository) : ViewModel(){
    private val state : SingleLiveEvent<ShoppingState> = SingleLiveEvent()
    private val orders = MutableLiveData<List<Order>>()

    private fun setLoading(){ state.value = ShoppingState.IsLoading(true) }
    private fun hideLoading(){ state.value = ShoppingState.IsLoading(false) }
    private fun toast(message: String){ state.value = ShoppingState.ShowToast(message) }
    private fun successPayment() { state.value = ShoppingState.SuccessPayment }

    fun getOrderByUser(token : String){
        setLoading()
        orderRepository.getOrderByUser(token){resultOrders, error ->
            hideLoading()
            error?.let { it.message?.let { message-> toast(message) } }
            resultOrders?.let { orders.postValue(it) }
        }
    }

    fun updateStatus(token: String, id : String, status : String){
        setLoading()
        orderRepository.updateStatus(token, id, status){resultBool, error ->
            hideLoading()
            error?.let { it.message?.let { message-> toast(message) } }
            if (resultBool){
                successPayment()
            }
        }
    }

    fun listenToState() = state
    fun listenToMyOrders() = orders
}

sealed class ShoppingState{
    data class IsLoading(var state : Boolean = false) : ShoppingState()
    data class ShowToast(var message : String) : ShoppingState()
    object SuccessPayment : ShoppingState()
}