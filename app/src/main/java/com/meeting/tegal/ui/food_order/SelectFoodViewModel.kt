package com.meeting.tegal.ui.food_order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meeting.models.Food
import com.example.meeting.utilities.SingleLiveEvent
import com.meeting.tegal.repository.FoodRepository
import com.meeting.tegal.utilities.ArrayResponse

class SelectFoodViewModel(private val foodRepository: FoodRepository) : ViewModel(){
    private val state : SingleLiveEvent<SelectFoodState> = SingleLiveEvent()
    private val foods = MutableLiveData<MutableList<Food>>()
    private val selectedFoods = MutableLiveData<MutableList<Food>>()

    private fun isLoading(b: Boolean){ state.value = SelectFoodState.Loading(b) }
    private fun toast(message: String){ state.value = SelectFoodState.ShowToast(message) }

    fun getFoods(token: String, partnerId: String){
        isLoading(true)
        foodRepository.getFoods(token, partnerId, object : ArrayResponse<Food>{
            override fun onSuccess(datas: List<Food>?) {
                isLoading(false)
                datas?.let {
                    foods.postValue(it as MutableList<Food>?)
                }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun addSelectedProduct(f: Food){
        val tempSelectedFoods = if(selectedFoods.value == null){
            mutableListOf()
        } else {
            selectedFoods.value as MutableList<Food>
        }
        val sameProduct = tempSelectedFoods.find { p -> p.id == f.id }
        sameProduct?.let {p ->
            p.qty = p.qty?.plus(1)
        } ?: kotlin.run {
            f.qty = 1
            tempSelectedFoods.add(f)
        }
        selectedFoods.postValue(tempSelectedFoods)
    }


    fun decrementQuantity(f: Food){
        val tempSelectedFoods = if(selectedFoods.value == null){
            mutableListOf()
        } else {
            selectedFoods.value as MutableList<Food>
        }
        val p = tempSelectedFoods.find { it.id == f.id }
        p?.let {
            if(it.qty?.minus(1) == 0){
                tempSelectedFoods.remove(it)
            }else{
                it.qty = it.qty!!.minus(1)
            }
        }
        selectedFoods.postValue(tempSelectedFoods)
    }

    fun incrementQuantity(f: Food){
        val tempSelectedFoods = if(selectedFoods.value == null){
            mutableListOf()
        } else {
            selectedFoods.value as MutableList<Food>
        }
        val p = tempSelectedFoods.find { it.id == f.id }
        p?.let {
            it.qty = it.qty!!.plus(1)
        }
        selectedFoods.postValue(tempSelectedFoods)
    }

    fun deleteSelectedProduct(f: Food){
        val tempSelectedFoods = if(selectedFoods.value == null){
            mutableListOf()
        } else {
            selectedFoods.value as MutableList<Food>
        }
        val x = tempSelectedFoods.find { it.id == f.id }
        x?.let {
            tempSelectedFoods.remove(it)
        }
        selectedFoods.postValue(tempSelectedFoods)
    }


    fun listenToState() = state
    fun listenToFoods() = foods
    fun listenToSelectedFoods() = selectedFoods
}

sealed class SelectFoodState{
    data class Loading(val isLoading: Boolean) : SelectFoodState()
    data class ShowToast(val message: String) : SelectFoodState()
}