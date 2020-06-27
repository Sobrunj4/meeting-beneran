package com.example.meeting.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Mitra(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("nama_mitra") var nama_mitra: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("no_hp") var password: String? = null,
    @SerializedName("nama_pemilik") var nama_pemilik: String? = null,
    @SerializedName("nama_bank") var nama_bank: String? = null,
    @SerializedName("nama_rekening") var nama_rekening: String? = null,
    @SerializedName("nama_akun_bank") var nama_akun_bank: String? = null,
    @SerializedName("alamat") var alamat: String? = null,
    @SerializedName("status") var status: Boolean? = false
) : Parcelable