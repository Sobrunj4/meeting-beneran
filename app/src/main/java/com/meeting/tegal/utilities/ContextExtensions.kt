package com.meeting.tegal.utilities

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.meeting.tegal.R

fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showInfoAlert(message: String){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton(resources.getString(R.string.ok)){ d, _ ->
            d.dismiss()
        }
    }.show()
}