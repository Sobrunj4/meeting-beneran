package com.meeting.tegal.ui.detail_harga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meeting.models.Food
import com.example.meeting.models.MeetingRoom
import com.example.meeting.utilities.Constants
import com.meeting.tegal.R
import com.meeting.tegal.models.CreateOrder
import com.meeting.tegal.ui.main.MainActivity
import com.meeting.tegal.utilities.disable
import com.meeting.tegal.utilities.enable
import kotlinx.android.synthetic.main.activity_detail_harga.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailHargaActivity : AppCompatActivity() {

    private val detailHargaViewModel : DetailHargaViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_harga)
        rv_detail_harga.apply {
            adapter = DetailHargaAdapter(mutableListOf(), this@DetailHargaActivity)
            layoutManager = LinearLayoutManager(this@DetailHargaActivity)
        }
        setUI()
        detailHargaViewModel.listenToState().observer(this, Observer { handleUI(it) })
        rv_detail_harga.adapter?.let {adapter ->
            if (adapter is DetailHargaAdapter) {
                adapter.changelist(getPassedFoodsSelected()!!)
            }
        }
        order()
    }

    private fun handleUI(it : DetailHargaState){
        when(it){
            is DetailHargaState.ShowToast -> toast(it.message)
            is DetailHargaState.IsLoading -> {
                if (it.state){
                    btn_order.disable()
                }else{
                    btn_order.enable()
                }
            }
            is DetailHargaState.Success -> {
                toast("Berhasil Order")
                startActivity(Intent(this@DetailHargaActivity, MainActivity::class.java))
            }
        }
    }

    private fun order(){
        btn_order.setOnClickListener {
            val order = CreateOrder(
                dateAndTIme = getPassedDateAndTime(),
                duration = getPassedDuration(),
                harga = getPassedRoom()?.harga_sewa,
                id_room = getPassedRoom()?.id,
                id_partner = getPassedRoom()?.partner!!.id,
                foods = getPassedFoodsSelected()!!
            )
            detailHargaViewModel.order(Constants.getToken(this@DetailHargaActivity), order)
        }
    }

    private fun setUI() {
        txt_harga_ruangan.text = Constants.setToIDR(getPassedRoom()?.harga_sewa!!)
        val priceFoods = getPassedFoodsSelected()?.sumBy {
            it.price!! * it.qty!!
        }
        txt_harga_makanan.text = Constants.setToIDR(priceFoods!!)
        txt_total_harga.text = Constants.setToIDR(getPassedRoom()!!.harga_sewa!!.plus(priceFoods))
    }

    private fun getPassedRoom() : MeetingRoom? = intent.getParcelableExtra("ROOM")
    private fun getPassedDateAndTime() = intent.getStringExtra("DATEANDTIME")
    private fun getPassedDuration() = intent.getStringExtra("DURATION")
    private fun getPassedFoodsSelected() : List<Food>? = intent.getParcelableArrayListExtra("FOODS")
    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
