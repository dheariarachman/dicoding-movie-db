package com.example.themovieapps.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Serial(
    var id: Int? = null,
    val title: String,
    val description: String,
    val imgPoster: String? = null,
    val years: String
) : Parcelable