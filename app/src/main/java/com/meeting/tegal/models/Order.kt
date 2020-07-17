package com.meeting.tegal.models

import android.os.Parcelable
import com.example.meeting.models.Food
import com.example.meeting.models.MeetingRoom
import com.example.meeting.models.User
import com.google.gson.annotations.SerializedName
import com.meeting.tegal.Partner
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("tanggal") var date : String? = null,
    @SerializedName("jam_mulai") var startHour : String? = null,
    @SerializedName("jam_selesai") var endHour : String? = null,
    @SerializedName("harga") var price : Int? = null,
    @SerializedName("total_bayar") var totalPrice : Int? = null,
    @SerializedName("snap_token") var snap : String? = null,
    @SerializedName("verifikasi") var verifikasi : String? = null,
    @SerializedName("status") var status : String? = null,
    @SerializedName("user") var user : User? = null,
    @SerializedName("ruang") var room : MeetingRoom? = null,
    @SerializedName("mitra") var partner: Partner? = null,
    @SerializedName("makanan") var foods : List<Food> = mutableListOf()
) : Parcelable

data class CreateOrder(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("tanggal_dan_waktu") var dateAndTIme : String? = null,
    @SerializedName("durasi") var duration : String? = null,
    @SerializedName("harga") var harga : Int? = null,
    @SerializedName("id_room") var id_room : Int?= null,
    @SerializedName("id_mitra") var id_partner : Int?= null,
    @SerializedName("makanans") var foods : List<Food> = mutableListOf()
)