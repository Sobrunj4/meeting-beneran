package com.meeting.tegal.ui.meeting


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.lifecycle.Observer
import com.example.meeting.utilities.Constants
import com.meeting.tegal.R
import com.meeting.tegal.ui.available_room.AvailableRoomActivity
import com.meeting.tegal.utilities.*
import kotlinx.android.synthetic.main.activity_meeting.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class MeetingActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private val meetingViewModel: MeetingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting)
        supportActionBar?.hide()
        observe()
        selectDateAndTime()

    }

    private fun selectDateAndTime(){
        tanggal_dan_waktu.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(this, this, year, month, day)
            datePickerDialog.show()
        }
    }

    private fun observe(){
        observeCurrentCalendar()
    }

    private fun observeCurrentCalendar() = meetingViewModel.listenToCurrentCalendar().observe(this, Observer { handleCurrentCalendar(it) })
    private fun setCalendarValue(calendar: Calendar) = meetingViewModel.setCurrentCalendar(calendar)
    private fun restoreOldCalendarValue() = meetingViewModel.restoreOldCalendarValue()
    private fun setCalendarTimeValue(hour: Int, minute: Int) = meetingViewModel.setCalendarTimeValue(hour, minute)
    private fun handleCurrentCalendar(calendar: Calendar?) = setTextOfDateField(calendar)


    private fun setTextOfDateField(calendar: Calendar?){
        if(calendar != null)
            tanggal_dan_waktu.setText(Constants.convertCalendarToFormattedDate(calendar))
        else
            tanggal_dan_waktu.text?.clear()
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        setCalendarValue(calendar)
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this, this, hour, minute, true)
        timePickerDialog.setOnCancelListener {
            restoreOldCalendarValue()
        }
        timePickerDialog.show()
    }


    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        setCalendarTimeValue(hourOfDay, minute)
        btn_search.enable()
        searchOnClick()
    }


    private fun searchOnClick(){
        btn_search.setOnClickListener {
            val dateAndTime = tanggal_dan_waktu.text.toString().trim()
            val duration = ed_lama.text.toString().trim()
            if(validate(dateAndTime, duration)){
                startActivity(Intent(this, AvailableRoomActivity::class.java).apply {
                    putExtra("date_and_time", dateAndTime)
                    putExtra("duration", duration)
                })
            }
        }
    }


    private fun validate(dateAndTime: String, duration: String): Boolean {
        if(dateAndTime.isBlank() or dateAndTime.isEmpty()){
            showInfoAlert(resources.getString(R.string.error_dateAndTime_empty))
            return false
        }
        if(duration.isEmpty()){
            showInfoAlert(resources.getString(R.string.error_duration_isEmptyOrZero))
            return false
        }
        return true
    }
}
