package com.meeting.tegal.ui.order

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meeting.models.Food
import com.example.meeting.models.User
import com.example.meeting.utilities.SingleLiveEvent
import com.meeting.tegal.models.CreateOrder
import com.meeting.tegal.models.Order
import com.meeting.tegal.repository.MeetingRepository
import com.meeting.tegal.repository.OrderRepository
import com.meeting.tegal.repository.UserRepository
import com.meeting.tegal.utilities.ArrayResponse
import com.meeting.tegal.utilities.SingleResponse
import java.text.SimpleDateFormat
import java.util.*

class OrderActivityViewModel (private val orderRepository: OrderRepository, private val userRepository: UserRepository) : ViewModel(){
    private val user = MutableLiveData<User>()
    private val order = MutableLiveData<Order>()
    private val state : SingleLiveEvent<OrderActivityState> = SingleLiveEvent()
    private val selectedFoods = MutableLiveData<List<Food>?>()

    private val currentSelectedDate = MutableLiveData<String>()
    private val currentSelectedStartTime = MutableLiveData<String>()
    private val currentSelectedEndTime = MutableLiveData<String>()

    private fun setLoading(){ state.value = OrderActivityState.Loading(true) }
    private fun hideLoading(){ state.value = OrderActivityState.Loading(false) }
    private fun toast(message : String) { state.value = OrderActivityState.ShowToast(message) }

    @SuppressLint("SimpleDateFormat")
    fun setCurrentDate(myCalendar: Calendar){
        val myFormat = "yyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        currentSelectedDate.value = sdf.format(myCalendar.time)
    }

    fun setCurrentStartTime(hour: Int, minute: Int) {
        currentSelectedStartTime.value = "$hour:$minute"
    }

    fun setCurrentEndTime(hour: Int, minute: Int) {
        currentSelectedEndTime.value = "$hour:$minute"
    }

    fun setSelectedFoods(foods: List<Food>){
        foods.forEach {
            println(it.name)
        }
        selectedFoods.value = foods
    }

    fun fetchProfile(token: String){
        setLoading()
        userRepository.profile(token, object: SingleResponse<User>{
            override fun onSuccess(data: User?) {
                hideLoading()
                data?.let { user.postValue(it) }
            }
            override fun onFailure(err: Error?) {
                hideLoading()
                err?.let { println(it.message) }
            }
        })
    }

    fun order(token: String, createOrder : CreateOrder){
        setLoading()
        orderRepository.order(token, createOrder ){b, error ->
            error?.let { it.message?.let { message -> toast(message) } }
            if (b){
                state.value = OrderActivityState.Success
            }
        }
    }

    fun listenToState() = state
    fun listenToUser() = user
    fun listenToCurrentStartTime() = currentSelectedStartTime
    fun listenToCurrentEndTime() = currentSelectedEndTime
    fun listenToCurrentDate() = currentSelectedDate
}

sealed class OrderActivityState {
    data class Loading(val isLoading: Boolean) : OrderActivityState()
    data class ShowToast(var message: String) : OrderActivityState()
    object Success : OrderActivityState()
}