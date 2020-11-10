package com.meeting.tegal.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meeting.models.MeetingRoom
import com.meeting.tegal.Partner
import com.meeting.tegal.R
import com.meeting.tegal.dialog.DialogSearch
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
        setUpRecyclerViewPromo()
        openDialogSearch()
        observe()
    }

    private fun openDialogSearch(){
        requireView().btn_search.setOnClickListener {
            val dialog = DialogSearch()
            dialog.show(requireActivity().supportFragmentManager, "dialog search")
        }
    }

    private fun setUpRecyclerViewPartner(){
        requireView().recycler_company.apply {
            adapter = HomeAdapter(mutableListOf(), requireActivity())
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun setUpRecyclerViewPromo() {
        requireView().recycler_promo.apply {
            adapter = PromoAdapter(mutableListOf(), requireActivity())
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observe() {
        observeState()
        observePartners()
        observePromos()
    }

    private fun observePartners() = homeViewModel.listenToPartners().observe(viewLifecycleOwner, Observer { handlePartners(it) })
    private fun observePromos() = homeViewModel.listenToPromoRooms().observe(viewLifecycleOwner, Observer { handlePromoRooms(it) })
    private fun observeState() = homeViewModel.listenToState().observe(viewLifecycleOwner, Observer { handleUiState(it) })
    private fun fetchPromo() = homeViewModel.fetchPromo()

    private fun handlePartners(list: List<Partner>?) {
        list?.let {
            requireView().recycler_company.adapter?.let { adapter ->
                if (adapter is HomeAdapter) adapter.changeList(it)
            }
        }
    }

    private fun handlePromoRooms(list: List<MeetingRoom>?) {
        list?.let {
            requireView().recycler_promo.adapter?.let { adapter ->
                if (adapter is PromoAdapter) adapter.changeList(it)
            }
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
        fetchPromo()
    }
}