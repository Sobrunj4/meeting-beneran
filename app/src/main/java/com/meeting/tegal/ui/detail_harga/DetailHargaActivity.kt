package com.meeting.tegal.ui.detail_harga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meeting.models.Food
import com.example.meeting.models.MeetingRoom
import com.example.meeting.utilities.Constants
import com.meeting.tegal.Partner
import com.meeting.tegal.R
import com.meeting.tegal.models.CreateOrder
import com.meeting.tegal.ui.login.LoginActivity
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
        setUpRecyclerViewDetailHarga()
        setField()
        detailHargaViewModel.listenToState().observer(this, Observer { handleUI(it) })
        updateRecyclerViewDetailHarga()
        order()
    }

    private fun setUpRecyclerViewDetailHarga(){
        rv_detail_harga.apply {
            adapter = DetailHargaAdapter(mutableListOf(), this@DetailHargaActivity)
            layoutManager = LinearLayoutManager(this@DetailHargaActivity)
        }
    }

    private fun updateRecyclerViewDetailHarga(){
        rv_detail_harga.adapter?.let {adapter ->
            if (adapter is DetailHargaAdapter) {
                adapter.changelist(getPassedFoodsSelected()!!)
            }
        }
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

    private fun order() {
        btn_order.setOnClickListener {
            if (isLoggedIn()){
                val token = Constants.getToken(this@DetailHargaActivity)
                val order = CreateOrder(
                    date = getPassedDate(),
                    startTime = getPassedStartTime(),
                    endTime = getPassedEndTime(),
                    harga = getPassedRoom()?.harga_sewa,
                    id_room = getPassedRoom()?.id,
                    id_partner = getPassedCompany()?.id,
                    foods = getPassedFoodsSelected()!!
                )
                detailHargaViewModel.order(token, order)
            }else{
                alertNotLogin("silahkan login dahulu")
            }
        }
    }

    fun alertNotLogin(message: String){
        AlertDialog.Builder(this).apply {
            setMessage(message)
            setPositiveButton("ya"){dialogInterface, _ ->
                startActivity(Intent(this@DetailHargaActivity, LoginActivity::class.java)
                    .putExtra("EXPECT_RESULT", true))
                dialogInterface.dismiss()
            }
        }.show()
    }

    private fun setField() {
        txt_harga_ruangan.text = Constants.setToIDR(getPassedRoom()?.harga_sewa!!)
        val priceFoods = getPassedFoodsSelected()?.sumBy { it.price!! * it.qty!! }
        txt_harga_makanan.text = Constants.setToIDR(priceFoods!!)
        txt_total_harga.text = Constants.setToIDR(getPassedRoom()!!.harga_sewa!!.plus(priceFoods))
    }

    private fun isLoggedIn() = Constants.getToken(this@DetailHargaActivity) != "UNDEFINED"
    private fun getPassedRoom() = intent.getParcelableExtra<MeetingRoom>("ROOM")
    private fun getPassedCompany() = intent.getParcelableExtra<Partner>("COMPANY")
    private fun getPassedDate() = intent.getStringExtra("DATE")
    private fun getPassedStartTime() = intent.getStringExtra("START_TIME")
    private fun getPassedEndTime() = intent.getStringExtra("END_TIME")
    private fun getPassedFoodsSelected() : List<Food>? = intent.getParcelableArrayListExtra("FOODS")
    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
