package com.meeting.tegal.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.meeting.models.Mitra
import com.example.meeting.models.RuangMeeting
import com.example.meeting.models.User
import com.example.meeting.utilities.Constants
import com.meeting.tegal.R
import com.meeting.tegal.extenstions.hidden
import com.meeting.tegal.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_pesan.*

class PesanActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private val REQUEST_CODE_PESAN_MAKANAN = 101

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesan)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.listenToUser().observe(this, Observer { handleUser(it) })

        getPassedRuangan()?.let {ruang->
            ed_nama_ruangan.hidden()
            ed_nama_ruangan.setText(ruang.nama_tempat)
            ed_harga.hidden()
            ed_harga.setText(ruang.harga_sewa.toString())

            ed_tanggal.hidden()
            ed_tanggal.setText("${getPassedTanggalDanWaktu()} / ${getPassedLama()} jam")

            txt_pesan_makanan.setOnClickListener {
                startActivityForResult(Intent(this@PesanActivity, PesanMakananActivity::class.java).apply {
                    putExtra("ID_MITRA", getPassedRuangan())
                }, REQUEST_CODE_PESAN_MAKANAN)
            }
        }
    }

    private fun handleUser(it : User) {
        ed_nama_user.setText(it.nama)
        ed_nohp_user.setText(it.no_hp)
        ed_email_user.setText(it.email)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PESAN_MAKANAN){
            toast(getResultOrderMakanan().toString())
            println(getResultOrderMakanan().toString())
        }
    }

    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    private fun getPassedRuangan() : RuangMeeting? = intent.getParcelableExtra("RUANGAN")
    private fun getPassedTanggalDanWaktu() = intent.getStringExtra("TANGGAL_DAN_WAKTU")
    private fun getPassedLama() = intent.getStringExtra("LAMA")
    private fun getResultOrderMakanan() : OrderMakananResult? = intent.getParcelableExtra("RESULT_ORDER_MAKANAN")

    override fun onResume() {
        super.onResume()
        userViewModel.profile(Constants.getToken(this@PesanActivity))
    }
}
