package com.meeting.tegal.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.meeting.tegal.Partner
import com.meeting.tegal.R
import com.meeting.tegal.utilities.gone
import com.meeting.tegal.utilities.toast
import com.meeting.tegal.utilities.visible
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel : HomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerViewPartner()
        observe()
        //setUpRecyclerViewPromo()
    }

    private fun setUpRecyclerViewPartner(){
        requireView().recycler_company.apply {
            adapter = HomeAdapter(mutableListOf(), requireActivity())
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun setUpRecyclerViewPromo() {
        requireView().recycler_promo.apply {
            adapter = HomeAdapter(mutableListOf(), requireActivity())
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observe() {
        observeState()
        observePartners()
    }

    private fun observePartners() = homeViewModel.listenToPartners().observe(viewLifecycleOwner, Observer { handlePartners(it) })
    private fun observeState() = homeViewModel.listenToState().observe(viewLifecycleOwner, Observer { handleUiState(it) })
    private fun fetchOwners() = homeViewModel.fetchPartners()

    private fun handlePartners(list: List<Partner>?) {
        list?.let {
            requireView().recycler_company.adapter?.let { adapter ->
                if (adapter is HomeAdapter) adapter.changeList(it)
            }
//            requireView().recycler_promo.adapter?.let { adapter ->
//                if (adapter is HomeAdapter) adapter.changeList(it)
//            }
        }
    }

    private fun handleUiState(state: HomeState?) {
        state?.let {
            when(it){
                is HomeState.Loading -> handleLoading(it.state)
                is HomeState.ShowToast -> requireActivity().toast(it.message)
            }
        }
    }

    private fun handleLoading(state: Boolean) = if (state) requireView().loading.visible() else requireView().loading.gone()

    override fun onResume() {
        super.onResume()
        fetchOwners()
        //fetchPromo()
    }
}