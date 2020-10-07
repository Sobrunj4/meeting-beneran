package com.meeting.tegal.ui.company

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.meeting.models.MeetingRoom
import com.meeting.tegal.Partner
import com.meeting.tegal.R
import com.meeting.tegal.ui.detail_meeting.DetailMeetingActivity
import com.meeting.tegal.utilities.gone
import com.meeting.tegal.utilities.toast
import com.meeting.tegal.utilities.visible
import kotlinx.android.synthetic.main.activity_company.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CompanyActivity : AppCompatActivity(), CompanyClickListener {
    private val companyViewModel : CompanyViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company)
        setUpRecyclerView()
        observe()
    }

    private fun setUpRecyclerView() {
        recycler_view.apply {
            adapter = CompanyAdapter(mutableListOf(), this@CompanyActivity)
            layoutManager = GridLayoutManager(this@CompanyActivity, 2)
        }
    }

    private fun observe() {
        observeState()
        observeRooms()
    }

    private fun observeState() = companyViewModel.listenToState().observe(this, Observer { handleUiState(it) })
    private fun observeRooms() = companyViewModel.listenToRooms().observe(this, Observer { handleRooms(it) })

    private fun handleRooms(list: List<MeetingRoom>?) {
        list?.let {
            recycler_view.adapter?.let { adapter ->
                if (adapter is CompanyAdapter){
                    adapter.changeList(it)
                }
            }
        }
    }

    private fun handleUiState(state: CompanyState?) {
        state?.let {
            when(it){
                is CompanyState.Loading -> handleLoading(it.state)
                is CompanyState.ShowToast -> toast(it.message)
            }
        }
    }

    private fun handleLoading(b: Boolean) = if (b) loading.visible() else loading.gone()

    private fun fetchRoomsByCompany() = companyViewModel.fetchRoomsByPartner(getPassedCompany()?.id.toString())
    private fun getPassedCompany() = intent.getParcelableExtra<Partner>("COMPANY")

    override fun onResume() {
        super.onResume()
        fetchRoomsByCompany()
    }

    override fun click(room: MeetingRoom) {
        startActivity(Intent(this, DetailMeetingActivity::class.java).apply {
            putExtra("ROOM", room)
            putExtra("COMPANY", getPassedCompany())
        })
    }
}