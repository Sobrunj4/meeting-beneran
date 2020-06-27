package com.example.meeting.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("nama") var nama : String? = null,
    @SerializedName("email") var email : String? = null,
    @SerializedName("password") var password : String? = null,
    @SerializedName("no_hp") var no_hp : String? = null,
    @SerializedName("api_token") var token : String? = null
) : Parcelable