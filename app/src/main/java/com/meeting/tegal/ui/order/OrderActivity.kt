package com.meeting.tegal.ui.order

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.meeting.models.Food
import com.example.meeting.models.MeetingRoom
import com.example.meeting.models.User
import com.example.meeting.utilities.Constants
import com.meeting.tegal.R
import com.meeting.tegal.extenstions.hidden
import com.meeting.tegal.models.CreateOrder
import com.meeting.tegal.models.Order
import com.meeting.tegal.ui.detail_harga.DetailHargaActivity
import com.meeting.tegal.ui.food_order.SelectFoodActivity
import com.meeting.tegal.utilities.toast
import kotlinx.android.synthetic.main.activity_order.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderActivity : AppCompatActivity() {
    companion object{ const val REQUEST_CODE_PESAN_MAKANAN = 101 }
    private var restrucutureFoods : ArrayList<Food> = arrayListOf()
    private val orderViewModel : OrderActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        observe()
        fill()
        selectFoodForResult()
        btn_konfirmasi.setOnClickListener {
            startActivity(Intent(this@OrderActivity, DetailHargaActivity::class.java).apply {
                putExtra("DATEANDTIME", getPassedDateAndTime())
                putExtra("DURATION", getPassedDuration())
                putExtra("ROOM", getPassedRoom())
                putExtra("FOODS", restrucutureFoods)
            })
        }
    }

    private fun observe(){
        observeState()
        observeUser()
    }

    private fun observeState() = orderViewModel.listenToState().observer(this, Observer { handleState(it) })
    private fun observeUser() = orderViewModel.listenToUser().observe(this, Observer { handleUser(it) })

    private fun handleState(state: OrderActivityState){
        when(state){
            is OrderActivityState.Loading -> isLoading(state.isLoading)
            is OrderActivityState.ShowToast -> toast(state.message)
            is OrderActivityState.Success -> finish()
        }
    }

    private fun isLoading(b: Boolean){
        if(b){

        }else{

        }
    }

    private fun handleUser(it : User) {
        ed_nama_user.setText(it.nama)
        ed_nohp_user.setText(it.no_hp)
        ed_email_user.setText(it.email)
    }

    private fun onSelectFoodsReturn(foods: List<Food>){
        toast("${foods.size} makanan ditambahkan")
        orderViewModel.setSelectedFoods(foods)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PESAN_MAKANAN){
            val foods = data?.getParcelableArrayListExtra<Food>("selected_foods")
            foods?.let {
                onSelectFoodsReturn(it)
            }
            restrucutureFoods = foods!!
        }
    }

    private fun getPassedRoom() : MeetingRoom? = intent.getParcelableExtra("RUANGAN")
    private fun getPassedDateAndTime() = intent.getStringExtra("TANGGAL_DAN_WAKTU")
    private fun getPassedDuration() = intent.getStringExtra("LAMA")

    @SuppressLint("SetTextI18n")
    private fun fill(){
        getPassedRoom()?.let { room ->
            ed_nama_ruangan.hidden()
            ed_nama_ruangan.setText(room.nama_tempat)
            ed_harga.hidden()
            ed_harga.setText(room.harga_sewa.toString())
            ed_tanggal.hidden()
            ed_tanggal.setText("${getPassedDateAndTime()} / ${getPassedDuration()} jam")


        }
    }


    private fun selectFoodForResult(){
        txt_pesan_makanan.setOnClickListener {
            startActivityForResult(Intent(this@OrderActivity, SelectFoodActivity::class.java).apply {
                putExtra("ID_MITRA", getPassedRoom()?.partner?.id.toString())
            }, REQUEST_CODE_PESAN_MAKANAN)
        }
    }


    override fun onResume() {
        super.onResume()
        orderViewModel.fetchProfile(Constants.getToken(this@OrderActivity))
    }
}
