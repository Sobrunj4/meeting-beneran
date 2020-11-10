package com.meeting.tegal.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Promo(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("harga_promo") var promo_price : Int? = null,
    @SerializedName("start_date") var startDate : String? = null,
    @SerializedName("end_date") var endDate : String? = null
) : Parcelable