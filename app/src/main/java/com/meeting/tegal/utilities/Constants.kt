package com.example.meeting.utilities

import android.content.Context
import android.content.Context.MODE_PRIVATE
import java.text.NumberFormat
import java.util.*

class Constants {
    companion object{
        const val END_POINT = "https://meeting-ning-tegal.herokuapp.com/api/"


        fun getToken(c : Context) : String {
            val s = c.getSharedPreferences("USER", MODE_PRIVATE)
            val token = s?.getString("TOKEN", "UNDEFINED")
            return token!!
        }

        fun setToken(context: Context, token : String){
            val pref = context.getSharedPreferences("USER", MODE_PRIVATE)
            pref.edit().apply {
                putString("TOKEN", token)
                apply()
            }
        }

        fun clearToken(context: Context){
            val pref = context.getSharedPreferences("USER", MODE_PRIVATE)
            pref.edit().clear().apply()
        }

        fun isValidEmail(email : String) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        fun isValidPassword(pass : String) = pass.length >= 8

        fun setToIDR(num : Int) : String {
            val localeID = Locale("in", "ID")
            val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
            return formatRupiah.format(num)
        }

        fun convertCalendarToFormattedDate(calendar: Calendar) : String {
            val year = calendar.get(Calendar.YEAR)
            //val month = calendar.get(Calendar.MONTH).plus(1)
            val month = if (calendar.get(Calendar.MONTH).plus(1) < 10){
                val temp = calendar.get(Calendar.MONTH).plus(1)
                "0$temp"
            }else{
                calendar.get(Calendar.MONTH).plus(1)
            }
            //val day = calendar.get(Calendar.DAY_OF_MONTH)
            val day = if (calendar.get(Calendar.DAY_OF_MONTH) < 10){
                val temp = calendar.get(Calendar.DAY_OF_MONTH)
                "0$temp"
            }else{
                calendar.get(Calendar.DAY_OF_MONTH).plus(1)
            }
            val hour = if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
                val temp = calendar.get(Calendar.HOUR_OF_DAY).toString()
                "0$temp"
            }else{
                calendar.get(Calendar.HOUR_OF_DAY).toString()
            }
            val minute = if(calendar.get(Calendar.MINUTE).toString().length == 1){
                val temp = calendar.get(Calendar.MINUTE)
                "0$temp"
            }else{
                calendar.get(Calendar.MINUTE).toString()
            }
            return "$year-$month-$day $hour:$minute"
        }
    }
}