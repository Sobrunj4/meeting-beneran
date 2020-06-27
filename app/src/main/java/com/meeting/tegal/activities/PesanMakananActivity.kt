package com.meeting.tegal.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meeting.models.Makanan
import com.example.meeting.models.RuangMeeting
import com.example.meeting.utilities.Constants
import com.meeting.tegal.MyClickListener
import com.meeting.tegal.R
import com.meeting.tegal.adapters.PesanMakananAdapter
import com.meeting.tegal.viewmodels.MakananState
import com.meeting.tegal.viewmodels.MakananViewModel
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_pesanmakanan.*

class PesanMakananActivity : AppCompatActivity(), MyClickListener {

    private var idMakanan : MutableList<Int> = mutableListOf()
    private lateinit var idMakananDistinct : List<Int>
    private var totalPesanMakanan : MutableList<Int> = mutableListOf()
    private lateinit var totalPesanMakananDistinct : List<String>

    private lateinit var makananViewModel: MakananViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanmakanan)

        rv_pesan_makanan.apply {
            adapter = PesanMakananAdapter(mutableListOf(), this@PesanMakananActivity)
            layoutManager = LinearLayoutManager(this@PesanMakananActivity)
        }
        makananViewModel = ViewModelProvider(this).get(MakananViewModel::class.java)
        makananViewModel.listenToState().observer(this, Observer { handleUI(it) })
        makananViewModel.listenToMakanan().observe(this, Observer { handleData(it) })
        orderMakanan()
    }

    override fun clickPesanMakanan(makanan: Makanan?, totalPesan: Int) {
        idMakanan.add(makanan!!.id!!)
        idMakananDistinct = idMakanan.distinct()
        println(idMakananDistinct)



        val result = OrderMakananResult()
        idMakanan.add(makanan.id!!)
        totalPesanMakanan.add(totalPesan)
        result.idMakanan = idMakanan.distinct()
        result.totalMakanan = totalPesanMakanan

        btn_lanjutkan.setOnClickListener {
            val intent = Intent()
            intent.putExtra("RESULT_ORDER_MAKANAN", result)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun orderMakanan(){

    }

    private fun handleUI(it: MakananState) {
        when (it) {
            is MakananState.IsLoading -> {
                if (it.state) {
                    pb_pesan_makanan.visibility = View.VISIBLE
                } else {
                    pb_pesan_makanan.visibility = View.GONE
                }
            }
            is MakananState.ShowToast -> toast(it.message)
        }
    }

    private fun handleData(it : List<Makanan>){
        rv_pesan_makanan.adapter?.let {adapter ->
            if (adapter is PesanMakananAdapter){
                adapter.changelist(it)
            }
        }
    }


    private fun toast(message : String) = Toast.makeText(this@PesanMakananActivity, message, Toast.LENGTH_SHORT).show()
    private fun getPassedIdMitra() : RuangMeeting? = intent.getParcelableExtra("ID_MITRA")

    override fun onResume() {
        super.onResume()
        makananViewModel.getMakanans(Constants.getToken(this@PesanMakananActivity), getPassedIdMitra()!!.mitra.id.toString())
    }
}

@Parcelize
data class OrderMakananResult(var idMakanan : List<Int> = mutableListOf(), var totalMakanan : List<Int> = mutableListOf()) : Parcelable