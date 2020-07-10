package com.meeting.tegal.ui.main.shopping

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meeting.utilities.Constants
import com.meeting.tegal.R
import com.meeting.tegal.models.Order
import com.meeting.tegal.utilities.gone
import com.meeting.tegal.utilities.visible
import kotlinx.android.synthetic.main.fragment_shopping.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShoppingFragment : Fragment(R.layout.fragment_shopping){
    private val shoppingViewModel : ShoppingViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        shoppingViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUI(it) })
        shoppingViewModel.listenToMyOrders().observe(viewLifecycleOwner, Observer { handleDataOrdersByUser(it) })
    }

    private fun handleUI(it: ShoppingState) {
        when(it){
            is ShoppingState.IsLoading -> {
                if (it.state){
                    pb_shooping.visible()
                }else{
                    pb_shooping.gone()
                }
            }
            is ShoppingState.ShowToast -> toast(it.message)
            is ShoppingState.SuccessPayment -> shoppingViewModel.getOrderByUser(Constants.getToken(requireActivity()))
        }
    }

    private fun handleDataOrdersByUser(it: List<Order>) {
        rv_shopping.adapter?.let { adapter ->
            if (adapter is ShoppingAdapter){
                adapter.changelist(it)
            }
        }
    }

    private fun setUpUI() {
        rv_shopping.apply {
            adapter = ShoppingAdapter(mutableListOf(), requireActivity(), shoppingViewModel)
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }


    private fun toast(message : String) = Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()

    override fun onResume() {
        super.onResume()
        shoppingViewModel.getOrderByUser(Constants.getToken(requireActivity()))
    }
}