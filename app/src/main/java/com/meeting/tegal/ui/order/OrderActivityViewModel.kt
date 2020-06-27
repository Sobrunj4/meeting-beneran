package com.meeting.tegal.ui.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meeting.models.Food
import com.example.meeting.models.User
import com.example.meeting.utilities.SingleLiveEvent
import com.meeting.tegal.repository.MeetingRepository
import com.meeting.tegal.repository.UserRepository
import com.meeting.tegal.utilities.SingleResponse

class OrderActivityViewModel (private val meetingRepository: MeetingRepository, private val userRepository: UserRepository) : ViewModel(){
    private val user = MutableLiveData<User>()
    private val state : SingleLiveEvent<OrderActivityState> = SingleLiveEvent()
    private val selectedFoods = MutableLiveData<List<Food>?>()

    private fun setLoading(){
        state.value = OrderActivityState.Loading(true)
    }
    private fun hideLoading(){
        state.value = OrderActivityState.Loading(false)
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

    fun listenToState() = state
    fun listenToUser() = user
}

sealed class OrderActivityState {
    data class Loading(val isLoading: Boolean) : OrderActivityState()
}