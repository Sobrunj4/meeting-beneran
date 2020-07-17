package com.meeting.tegal.ui.food_order

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meeting.models.Food
import com.example.meeting.utilities.Constants
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.meeting.tegal.R
import com.meeting.tegal.utilities.gone
import com.meeting.tegal.utilities.toast
import com.meeting.tegal.utilities.visible
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_select_food.*
import kotlinx.android.synthetic.main.bottom_sheet_selected_foods.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList

class SelectFoodActivity : AppCompatActivity(), FoodClickInterface{
    private val foodViewModel: SelectFoodViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_food)
        setupRecyclerView()
        observe()
        fetchFoods()
        onFinishSelectFood()
    }

    private fun onFinishSelectFood(){
        btn_ok.setOnClickListener {
            val selectedFoods = foodViewModel.listenToFoods().value
            if(selectedFoods.isNullOrEmpty()){
                finish()
            }else{
                val i = Intent()
                i.putParcelableArrayListExtra("selected_foods", selectedFoods as ArrayList<out Parcelable>)
                setResult(Activity.RESULT_OK, i)
                finish()
            }
        }
    }

    private fun getPassedIdPartner() = intent.getStringExtra("ID_MITRA")


    private fun setupRecyclerView(){
        rv_foods.apply {
            adapter = FoodAdapter(mutableListOf(), this@SelectFoodActivity)
            layoutManager = LinearLayoutManager(this@SelectFoodActivity)
        }
    }


    private fun observe(){
        observeState()
        observeFoods()
    }

    private fun fetchFoods() = foodViewModel.getFoods(Constants.getToken(this), getPassedIdPartner())
    private fun observeFoods() = foodViewModel.listenToFoods().observe(this, Observer { handleFoods(it) })
    private fun observeState() = foodViewModel.listenToState().observer(this, Observer { handleState(it) })


    private fun handleState(state: SelectFoodState){
        when(state){
            is SelectFoodState.Loading -> isLoading(state.isLoading)
            is SelectFoodState.ShowToast -> toast(state.message)
        }
    }

    private fun handleFoods(foods: List<Food>){
        rv_foods.adapter?.let { adapter ->
            if(adapter is FoodAdapter){
                adapter.updateList(foods)
            }
        }
    }

    private fun isLoading(b: Boolean){
        if(b){
            loading.visible()
        }else{
            loading.gone()
        }
    }

    override fun click(food: Food) = foodViewModel.addSelectedProduct(food)
    override fun increment(food: Food)  = foodViewModel.incrementQuantity(food)
    override fun decrement(food: Food) = foodViewModel.decrementQuantity(food)

}