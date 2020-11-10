package com.meeting.tegal.ui.order

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.example.meeting.models.Food
import com.example.meeting.models.MeetingRoom
import com.example.meeting.models.User
import com.example.meeting.utilities.Constants
import com.meeting.tegal.Partner
import com.meeting.tegal.R
import com.meeting.tegal.extenstions.hidden
import com.meeting.tegal.ui.detail_harga.DetailHargaActivity
import com.meeting.tegal.ui.food_order.SelectFoodActivity
import com.meeting.tegal.utilities.toast
import kotlinx.android.synthetic.main.activity_order.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class OrderActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    companion object{ const val REQUEST_CODE_PESAN_MAKANAN = 101 }
    private var restrucutureFoods : ArrayList<Food> = arrayListOf()
    private val orderViewModel : OrderActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        observe()
        fill()
        setDateAndTime()
        selectFoodForResult()
        btn_konfirmasi.setOnClickListener {
            startActivity(Intent(this@OrderActivity, DetailHargaActivity::class.java).apply {
                putExtra("DATE", ed_tanggal.text.toString().trim())
                putExtra("START_TIME", jam_mulai.text.toString().trim())
                putExtra("END_TIME", jam_selesai.text.toString().trim())
                putExtra("ROOM", getPassedRoom())
                putExtra("COMPANY", getPassedCompany())
                putExtra("FOODS", restrucutureFoods)
            })
        }
    }

    private fun setDateAndTime(){
        openDatePicker()
        jam_mulai.setOnClickListener { openTimePickerStartTime() }
        jam_selesai.setOnClickListener { openTimePickerEndTime() }
    }

    private fun openTimePickerStartTime() {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]

        val mTimePicker: TimePickerDialog
        mTimePicker =
            TimePickerDialog(this@OrderActivity, TimePickerDialog.OnTimeSetListener { _, i, i2 ->
                if (i < 8) alertError("maaf, jam yang di masukan harus lebih dari jam 8", true)
                setCurrentStartTime(i, i2)
            }, hour, minute, true)
        mTimePicker.setTitle("Pilih Jam Selesai")
        mTimePicker.show()
    }

    private fun openTimePickerEndTime() {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]

        val mTimePicker: TimePickerDialog
        mTimePicker =
            TimePickerDialog(this@OrderActivity, TimePickerDialog.OnTimeSetListener { _, i, i2 ->
                if (i > 16) alertError(
                    "maaf, jam yang di masukan harus kurang dari jam 16 atau jam 4 sore",
                    false
                )
                setCurrentEndTime(i, i2)
            }, hour, minute, true)
        mTimePicker.setTitle("Pilih Jam Selesai")
        mTimePicker.show()
    }

    private fun alertError(m: String, b : Boolean) {
        AlertDialog.Builder(this).apply {
            setMessage(m)
            setPositiveButton("ya"){dialogInterface, _ ->
                dialogInterface.dismiss()
                if (b) openTimePickerStartTime() else openTimePickerEndTime()
            }
        }.show()
    }

    private fun openDatePicker(){
        ed_tanggal.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(this, this, year, month, day)
            datePickerDialog.datePicker.minDate = calendar.timeInMillis
            datePickerDialog.show()
        }
    }


    private fun observe(){
        observeState()
        observeUser()
        observeCurrentDate()
        observeCurrentStartTime()
        observeCurrentEndTime()
    }

    private fun setCalendarValue(calendar: Calendar) = orderViewModel.setCurrentDate(calendar)
    private fun setCurrentStartTime(hour: Int, minute: Int){ orderViewModel.setCurrentStartTime(hour, minute) }
    private fun setCurrentEndTime(hour: Int, minute: Int){ orderViewModel.setCurrentEndTime(hour, minute) }

    private fun observeState() = orderViewModel.listenToState().observer(this, Observer { handleState(it) })
    private fun observeUser() = orderViewModel.listenToUser().observe(this, Observer { handleUser(it) })
    private fun observeCurrentDate() = orderViewModel.listenToCurrentDate().observe(this, Observer { handleCurrentDate(it) })
    private fun observeCurrentStartTime() = orderViewModel.listenToCurrentStartTime().observe(this, Observer { handleCurrentStartTime(it) })
    private fun observeCurrentEndTime() = orderViewModel.listenToCurrentEndTime().observe(this, Observer { handleCurrentEndTime(it) })

    private fun handleCurrentDate(s: String?) = setTextOfDateField(s)
    private fun handleCurrentStartTime(s : String?) = setTextOfStartTime(s)
    private fun handleCurrentEndTime(s : String?) = setTextOfEndTime(s)

    private fun setTextOfDateField(s: String?) = if(s != null) ed_tanggal.setText(s) else ed_tanggal.text?.clear()
    private fun setTextOfStartTime(s : String?)  = if (s != null) jam_mulai.setText(s) else jam_mulai.text?.clear()
    private fun setTextOfEndTime(s : String?)  = if (s != null) jam_selesai.setText(s) else jam_selesai.text?.clear()

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
            foods?.let { onSelectFoodsReturn(it) }
            restrucutureFoods = foods!!
        }
    }

    @SuppressLint("SetTextI18n")
    private fun fill(){
        getPassedRoom()?.let { room ->
            ed_nama_ruangan.hidden()
            ed_nama_ruangan.setText(room.nama_tempat)
            ed_harga.hidden()
            ed_harga.setText(room.harga_sewa.toString())
            ed_tanggal.hidden()
        }
    }

    private fun selectFoodForResult(){
        txt_pesan_makanan.setOnClickListener {
            startActivityForResult(Intent(this@OrderActivity, SelectFoodActivity::class.java).apply {
                putExtra("ID_MITRA", getPassedCompany()?.id.toString())
            }, REQUEST_CODE_PESAN_MAKANAN)
        }
    }


    private fun getPassedRoom() = intent.getParcelableExtra<MeetingRoom>("ROOM")
    private fun getPassedCompany() = intent.getParcelableExtra<Partner>("COMPANY")

    override fun onResume() {
        super.onResume()
        orderViewModel.fetchProfile(Constants.getToken(this@OrderActivity))
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        setCalendarValue(calendar)
    }
}
