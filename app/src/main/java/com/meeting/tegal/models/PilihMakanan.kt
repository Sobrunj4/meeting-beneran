package com.meeting.tegal.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PilihMakanan(
    var id : Int?= null,
    var qty : Int? = null
) : Parcelable

var makanans : List<PilihMakanan> = mutableListOf()