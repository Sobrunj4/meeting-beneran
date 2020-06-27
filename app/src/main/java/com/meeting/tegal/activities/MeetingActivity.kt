package com.meeting.tegal.activities


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meeting.models.RuangMeeting
import com.example.meeting.utilities.Constants
import com.meeting.tegal.MyClickInterface
import com.meeting.tegal.R
import com.meeting.tegal.adapters.MeetingAdapter
import com.meeting.tegal.viewmodels.RuangMeetingState
import com.meeting.tegal.viewmodels.RuangMeetingViewModel
import kotlinx.android.synthetic.main.activity_meeting.*
import java.util.*


class MeetingActivity : AppCompatActivity(), MyClickInterface, DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var dateAndTime : String
    private lateinit var lama : String

    private var day : String? = null
    private var month : String? = null
    private var year : String? = null
    private var hour : String? = null
    private var minute : String? = null

    private var myDay : String? = null
    private var myMonth : String? = null
    private var myYear : String? = null
    private var myHour : String? = null
    private var myMinute : String? = null

    private lateinit var ruangMeetingViewModel: RuangMeetingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting)
        rv_meeting.apply {
            layoutManager = LinearLayoutManager(this@MeetingActivity)
            adapter = MeetingAdapter(mutableListOf(), this@MeetingActivity)
        }
        ruangMeetingViewModel = ViewModelProvider(this).get(RuangMeetingViewModel::class.java)
        tanggal_dan_waktu.setOnClickListener {
            val calendar = Calendar.getInstance()
            year = calendar.get(Calendar.YEAR).toString()
            month = calendar.get(Calendar.MONTH).toString();
            day = calendar.get(Calendar.DAY_OF_MONTH).toString();
            val datePickerDialog = DatePickerDialog(this@MeetingActivity,
                this@MeetingActivity, year!!.toInt(), month!!.toInt(), day!!.toInt())
            datePickerDialog.show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myYear = year.toString()
        myDay = day.toString()
        myMonth = month.toString()
        val c = Calendar.getInstance()
        hour = c.get(Calendar.HOUR).toString()
        minute = c.get(Calendar.MINUTE).toString()
        val timePickerDialog = TimePickerDialog(this@MeetingActivity,
            this@MeetingActivity, hour!!.toInt(), minute!!.toInt(), true)
        timePickerDialog.show()
    }


    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val months = resources.getStringArray(R.array.months)
        myHour = hourOfDay.toString();
        if(myHour!!.length == 1){
            myHour = "0$myHour"
        }
        myMinute = minute.toString();
        if (myMinute!!.length == 1) {
            myMinute = "0$myMinute"
        }
        tanggal_dan_waktu.text = "${myYear}-${myMonth!!.toInt()+1}-${myDay} $myHour:$myMinute"
        btn_search.isEnabled = true
        search()
    }

    private fun search(){
        btn_search.setOnClickListener {
            dateAndTime = tanggal_dan_waktu.text.toString().trim()
            lama = ed_lama.text.toString().trim()
            ruangMeetingViewModel.search(Constants.getToken(this@MeetingActivity), dateAndTime, lama)

            ruangMeetingViewModel.getState().observer(this@MeetingActivity, Observer { handleUi(it) })
            ruangMeetingViewModel.getRuangMeetings().observe(this@MeetingActivity, Observer {
                rv_meeting.adapter?.let {adapter ->
                    if (adapter is MeetingAdapter){
                        adapter.changelist(it)
                    }
                }
            })

        }
    }

    private fun handleUi(it : RuangMeetingState){
        when(it){
            is RuangMeetingState.IsLoading -> {
                if (it.state){
                    pb_meeting.visibility = View.VISIBLE
                    pb_meeting.isIndeterminate = true
                }else{
                    pb_meeting.visibility = View.GONE
                    pb_meeting.isIndeterminate = false
                }
            }
            is RuangMeetingState.ShowToast -> toast(it.message)
        }
    }

    override fun click(ruangMeeting: RuangMeeting) {
        startActivity(Intent(this@MeetingActivity, DetailMeetingActivity::class.java).apply {
            putExtra("MEETING", ruangMeeting)
            putExtra("TANGGAL_DAN_WAKTU", dateAndTime)
            putExtra("LAMA", lama)
        })
    }

    private fun toast(message : String) = Toast.makeText(this@MeetingActivity, message, Toast.LENGTH_SHORT).show()
}
