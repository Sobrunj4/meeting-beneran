package com.meeting.tegal.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meeting.models.Makanan
import com.example.meeting.utilities.SingleLiveEvent
import com.example.meeting.utilities.WrappedListResponse
import com.meeting.tegal.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakananViewModel : ViewModel(){
    private var makanans = MutableLiveData<List<Makanan>>()
    private var selectedMakanan = MutableLiveData<List<Makanan>>()
    private var state : SingleLiveEvent<MakananState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    private fun setLoading() { state.value = MakananState.IsLoading(true) }
    private fun hideLoading() { state.value = MakananState.IsLoading(false) }
    private fun toast(message: String) { state.value = MakananState.ShowToast(message) }


    fun getMakanans(token : String, id_mitra : String){
        setLoading()
        api.getMakanan(token, id_mitra.toInt()).enqueue(object : Callback<WrappedListResponse<Makanan>>{
            override fun onFailure(call: Call<WrappedListResponse<Makanan>>, t: Throwable) {
                println(t.message)
                hideLoading()
            }

            override fun onResponse(call: Call<WrappedListResponse<Makanan>>, response: Response<WrappedListResponse<Makanan>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        makanans.postValue(data)
                    }else{
                        println("b : ${body.message}")
                    }
                }else{
                    println("r : ${response.message()}")
                }
                hideLoading()
            }

        })
    }

    fun addSelectedMakanan(makanan: Makanan){
        val tempSelectedMakanan = if (selectedMakanan.value == null){
            mutableListOf()
        }else{
            selectedMakanan.value as MutableList<Makanan>
        }
        val sameMakanan = tempSelectedMakanan.find { m ->m.id == makanan.id }
        sameMakanan?.let {makanan ->
            makanan.se
        }
    }

    fun listenToState() = state
    fun listenToMakanan() = makanans
}

sealed class MakananState{
    data class ShowToast(var message : String) : MakananState()
    data class IsLoading(var state : Boolean) : MakananState()
}