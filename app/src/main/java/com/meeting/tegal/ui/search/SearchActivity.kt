package com.meeting.tegal.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.meeting.tegal.Partner
import com.meeting.tegal.R
import com.meeting.tegal.dialog.DialogSearch
import com.meeting.tegal.utilities.gone
import com.meeting.tegal.utilities.toast
import com.meeting.tegal.utilities.visible
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private val searchViewModel : SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        openDialogSearch()
        setUpRecyclerView()
        observe()
        searchPartners()
    }

    private fun openDialogSearch(){
        et_search.setOnClickListener {
            val dialog = DialogSearch()
            dialog.show(supportFragmentManager, "dialog search")
        }
    }

    private fun setUpRecyclerView(){
        recycler_view.apply {
            adapter = SearchAdapter(mutableListOf(), this@SearchActivity)
            layoutManager = GridLayoutManager(this@SearchActivity, 2)
        }
    }

    private fun observe(){
        observeState()
        observePartners()
    }

    private fun observeState() = searchViewModel.listenToState().observe(this, Observer { handleUiState(it) })
    private fun observePartners() = searchViewModel.listenToPartners().observe(this, Observer { handlePartners(it) })
    private fun searchPartners() = searchViewModel.searchPartners(getPassedDate(), getPassedStartTime(), getPassedEndTime())

    private fun getPassedDate() = intent.getStringExtra("DATE")
    private fun getPassedStartTime() = intent.getStringExtra("START_TIME")
    private fun getPassedEndTime() = intent.getStringExtra("END_TIME")

    private fun handlePartners(list: List<Partner>?) {
        list?.let {
            recycler_view.adapter?.let { adapter ->
                if (adapter is SearchAdapter){
                    adapter.changeList(it)
                }
            }
        }
    }

    private fun handleUiState(state: SearchState?) {
        state?.let {
            when(it){
                is SearchState.Loading -> handleLoading(it.state)
                is SearchState.ShowToast -> toast(it.message)
            }
        }
    }

    private fun handleLoading(b: Boolean) {
        if (b) loading.visible() else loading.gone()
    }
}