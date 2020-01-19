package com.example.themovieapps.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    var id: Int?,
    val title: String,
    val description: String? = null,
    val imgPoster: String?,
    val years: String? = null
) : Parcelable