package com.meeting.tegal.ui.main.shopping

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meeting.utilities.Constants
import com.meeting.tegal.R
import com.meeting.tegal.models.Order
import com.meeting.tegal.utilities.gone
import com.meeting.tegal.utilities.toast
import com.meeting.tegal.utilities.visible
import kotlinx.android.synthetic.main.fragment_shopping.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShoppingFragment : Fragment(R.layout.fragment_shopping){
    private val shoppingViewModel : ShoppingViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observe()
    }

    private fun observe(){
        observeState()
        observeOrder()
    }

    private fun observeState() = shoppingViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUiState(it) })
    private fun observeOrder() = shoppingViewModel.listenToMyOrders().observe(viewLifecycleOwner, Observer { handleDataOrdersByUser(it) })
    private fun fetchOrderByUser() = shoppingViewModel.getOrderByUser(Constants.getToken(requireActivity()))

    private fun handleUiState(it: ShoppingState) {
        when(it){
            is ShoppingState.IsLoading -> handleLoading(it.state)
            is ShoppingState.ShowToast -> requireActivity().toast(it.message)
            is ShoppingState.SuccessPayment -> fetchOrderByUser()
            is ShoppingState.SuccessCancel -> handleSuccessCancel()
        }
    }

    private fun handleSuccessCancel() {
        requireActivity().toast("berhasil cancel order")
        fetchOrderByUser()
    }

    private fun handleLoading(state: Boolean){
        if (state) {
            requireView().pb_shooping.visible()
        }else{
            requireView().pb_shooping.gone()
        }
    }

    private fun handleDataOrdersByUser(it: List<Order>) {
        requireView().rv_shopping.adapter?.let { adapter ->
            if (adapter is ShoppingAdapter){
                adapter.changelist(it)
            }
        }
    }

    private fun setUpRecyclerView() {
        requireView().rv_shopping.apply {
            adapter = ShoppingAdapter(mutableListOf(), requireActivity(), shoppingViewModel)
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    override fun onResume() {
        super.onResume()
        fetchOrderByUser()
    }
}