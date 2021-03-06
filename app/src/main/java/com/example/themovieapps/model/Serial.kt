package com.example.themovieapps.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Serial(
    var id: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val imgPoster: String? = null,
    val years: String? = null
) : Parcelable