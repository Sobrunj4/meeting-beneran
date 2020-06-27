package com.example.meeting.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Makanan(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("nama") var nama : String? = null,
    @SerializedName("harga") var harga : Int? = null,
    @SerializedName("foto") var foto : String?= null,

    var isChecked : Boolean = false,
    var qty : Int? = 0
) : Parcelable