package com.example.meeting.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RuangMeeting(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("nama_tempat") var nama_tempat: String? = null,
    @SerializedName("kapasitas") var kapasitas: Int? = null,
    @SerializedName("harga_sewa") var harga_sewa: Int? = null,
    @SerializedName("foto") var foto: String? = null,
    @SerializedName("keterangan") var keterangan: String? = null,
    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("mitra") var mitra : Mitra
) : Parcelable