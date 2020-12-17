package com.meeting.tegal.dialog

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.meeting.tegal.R
import com.meeting.tegal.ui.search.SearchActivity
import kotlinx.android.synthetic.main.dialog_search.view.*
import java.util.*

class DialogSearch : DialogFragment(), DatePickerDialog. OnDateSetListener {

    private var startTime : String? = null
    private var endTime : String? = null
    private var date : String? = null
    private lateinit var dialogView: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_search, null)
        return MaterialAlertDialogBuilder(requireActivity())
            .setView(dialogView)
            .setTitle("cari")
            .setNegativeButton("cancel"){ d, _ -> d.dismiss() }
            .setPositiveButton("cari"){ dialog, _ ->
                if (date.isNullOrEmpty() || startTime.isNullOrEmpty() || endTime.isNullOrEmpty()){
                    alertNull("harus di isi semua")
                }else{
                    startActivity(Intent(requireActivity(), SearchActivity::class.java).apply {
                        putExtra("DATE", date)
                        putExtra("START_TIME", startTime)
                        putExtra("END_TIME", endTime)
                    })
                    dialog.dismiss()
                }
            }.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = dialogView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDateAndTime()
    }

    private fun openDatePicker(){
        requireView().ed_tanggal.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(requireActivity(), this, year, month, day)
            datePickerDialog.datePicker.minDate = calendar.timeInMillis
            datePickerDialog.show()
        }
    }

    private fun setDateAndTime(){
        openDatePicker()
        requireView().jam_mulai.setOnClickListener { openTimePickerStartTime() }
        requireView().jam_selesai.setOnClickListener { openTimePickerEndTime() }
    }

    @SuppressLint("SetTextI18n")
    private fun openTimePickerStartTime() {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]

        val mTimePicker: TimePickerDialog
        mTimePicker =
            TimePickerDialog(requireActivity(), TimePickerDialog.OnTimeSetListener { _, i, i2 ->
                if (i < 8) alertError("maaf, jam yang di masukan harus lebih dari jam 8", true)
                else if (i > 16) alertError(
                    "maaf, jam yang di masukan harus kurang dari jam 16 atau jam 4 sore",
                    false
                )
                requireView().jam_mulai.setText("${i}:${i2}")
                //startTime = "${i}:${i2}"
                startTime = i.toString()
                //setCurrentStartTime(i, i2)
            }, hour, minute, true)
        mTimePicker.setTitle("Pilih Jam Selesai")
        mTimePicker.show()
    }

    @SuppressLint("SetTextI18n")
    private fun openTimePickerEndTime() {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]

        val mTimePicker: TimePickerDialog
        mTimePicker =
            TimePickerDialog(requireActivity(), TimePickerDialog.OnTimeSetListener { _, i, i2 ->
                if (startTime.isNullOrEmpty() || i == null){
                    alertError("maaf, jam mulai dan jam selesai harus di isi", true)
                }else{
                    when {
                        i < 8 -> alertError("maaf, jam yang di masukan harus lebih dari jam 8", true)
                        i > 16 -> alertError(
                            "maaf, jam yang di masukan harus kurang dari jam 16 atau jam 4 sore",
                            false
                        )
                        startTime!!.toInt() > i -> alertError(
                            "maaf, jam yang di masukan harus kurang dari jam mulai",
                            false
                        )
                        startTime!!.toInt() == i -> alertError(
                            "maaf, jam yang di masukan tidak boleh sama",
                            false
                        )
                        //setCurrentEndTime(i, i2)
                    }
                    requireView().jam_selesai.setText("${i}:${i2}")
                    endTime = i.toString()
                }
                //setCurrentEndTime(i, i2)
            }, hour, minute, true)
        mTimePicker.setTitle("Pilih Jam Selesai")
        mTimePicker.show()
    }

    private fun alertNull(m : String){
        AlertDialog.Builder(requireActivity()).apply {
            setMessage(m)
            setPositiveButton("ya"){dialog, _->
                dialog.dismiss()
            }
        }.show()
    }

    private fun alertError(m: String, b : Boolean) {
        AlertDialog.Builder(requireActivity()).apply {
            setMessage(m)
            setPositiveButton("ya"){dialogInterface, _ ->
                dialogInterface.dismiss()
                if (b) openTimePickerStartTime() else openTimePickerEndTime()
            }
        }.show()
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        requireView().ed_tanggal.setText("$year, $month, $dayOfMonth")
        date = "$year-${month+1}-$dayOfMonth"
        //if (date.isEmpty()) alertError("harus memasukan tanggal", false)
    }
}