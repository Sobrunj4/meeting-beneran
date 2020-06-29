package com.meeting.tegal.ui.available_room

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meeting.models.MeetingRoom
import com.example.meeting.utilities.Constants
import com.meeting.tegal.R
import com.meeting.tegal.ui.detail_meeting.DetailMeetingActivity
import com.meeting.tegal.utilities.gone
import com.meeting.tegal.utilities.toast
import com.meeting.tegal.utilities.visible
import kotlinx.android.synthetic.main.activity_available_room.*
import kotlinx.android.synthetic.main.content_available_room.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AvailableRoomActivity : AppCompatActivity(), AvailableRoomInterface {
    private val availableRoomViewModel: AvailableRoomViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_available_room)
        setSupportActionBar(toolbar)
        setupToolbar()
        setupRecyclerView()
        observe()
        doSearch()
        setHelperTextView()
    }

    private fun getPassedDate() = intent.getStringExtra("date_and_time")
    private fun getPassedDuration() = intent.getStringExtra("duration")
    private fun doSearch() = availableRoomViewModel.searchMeetingRooms(Constants.getToken(this), getPassedDate(), getPassedDuration())
    private fun observeState() = availableRoomViewModel.listenToState().observer(this, Observer { handleState(it) })
    private fun observeRooms() = availableRoomViewModel.listenToRooms().observe(this, Observer { handleRooms(it) })

    private fun setHelperTextView(){
        availableRoom_date_textView.text = getPassedDate()
        availableRoom_duration_textView.text = "Untuk ${getPassedDuration()} jam"
    }

    private fun observe(){
        observeState()
        observeRooms()
    }


    private fun handleState(state: AvailableRoomState){
        when(state){
            is AvailableRoomState.Loading -> isLoading(state.isLoading)
            is AvailableRoomState.ShowToast -> toast(state.message)
        }
    }

    private fun isLoading(b: Boolean){
        if(b){
            loading.visible()
        }else{
            loading.gone()
        }
    }

    private fun handleRooms(rooms: List<MeetingRoom>){
        rv_availableRoom.adapter?.let { adapter ->
            if(adapter is AvailableRoomAdapter){
                adapter.changeList(rooms)
            }
        }
    }

    private fun setupRecyclerView(){
        rv_availableRoom.apply {
            layoutManager = LinearLayoutManager(this@AvailableRoomActivity)
            adapter = AvailableRoomAdapter(mutableListOf(), this@AvailableRoomActivity)
        }
    }


    private fun setupToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener { finish() }
    }

    override fun click(room: MeetingRoom) {
        startActivity(Intent(this, DetailMeetingActivity::class.java).apply {
            putExtra("MEETING", room)
            putExtra("TANGGAL_DAN_WAKTU", getPassedDate())
            putExtra("LAMA", getPassedDuration())
        })
    }
}