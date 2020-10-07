package com.meeting.tegal

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Partner(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("nama_mitra") var nama_mitra: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("no_hp") var password: String? = null,
    @SerializedName("foto") var image: String? = null,
    @SerializedName("alamat") var alamat: String? = null,
    @SerializedName("lat") var lat: String? = null,
    @SerializedName("lng") var lng: String? = null,
    @SerializedName("status") var status: Boolean? = false
) : Parcelable