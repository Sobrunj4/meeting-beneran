package com.meeting.tegal.models

import android.os.Parcelable
import com.example.meeting.models.Food
import com.example.meeting.models.MeetingRoom
import com.example.meeting.models.User
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("tanggal_dan_waktu") var dateAndTIme : String? = null,
    @SerializedName("tanggal") var duration : String? = null,
    @SerializedName("user") var user : User? = null,
    @SerializedName("room") var room : MeetingRoom? = null,
    @SerializedName("makanan") var foods : List<Food> = mutableListOf()
) : Parcelable

data class CreateOrder(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("tanggal_dan_waktu") var dateAndTIme : String? = null,
    @SerializedName("durasi") var duration : String? = null,
    @SerializedName("harga") var harga : Int? = null,
    @SerializedName("id_room") var id_room : Int?= null,
    @SerializedName("makanans") var foods : List<Food> = mutableListOf()
)