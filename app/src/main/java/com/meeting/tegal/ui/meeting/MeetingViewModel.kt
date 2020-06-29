package com.meeting.tegal.ui.meeting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class MeetingViewModel : ViewModel(){
    private val currentSelectedCalendar = MutableLiveData<Calendar>()
    private val oldCalendarValue = MutableLiveData<Calendar>()

    fun setCurrentCalendar(calendar: Calendar){
        currentSelectedCalendar.value?.let {
            oldCalendarValue.value = it
        }
        currentSelectedCalendar.value = calendar
    }

    fun setCalendarTimeValue(hour: Int, minute: Int){
        currentSelectedCalendar.value?.let {
            val calendar = Calendar.getInstance()
            calendar.set(it.get(Calendar.YEAR), it.get(Calendar.MONTH), it.get(Calendar.DAY_OF_MONTH), hour, minute)
            currentSelectedCalendar.value = calendar
        }
    }

    fun restoreOldCalendarValue(){
        oldCalendarValue.value?.let {
            currentSelectedCalendar.value = it
        } ?: run {
            currentSelectedCalendar.value = null
        }
    }

    fun listenToCurrentCalendar() = currentSelectedCalendar
}