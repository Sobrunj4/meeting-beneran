package com.example.meeting.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Food(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("nama") var name : String? = null,
    @SerializedName("harga") var price : Int? = null,
    @SerializedName("foto") var image : String?= null,

    var isChecked : Boolean = false,
    var qty : Int? = 0
) : Parcelable