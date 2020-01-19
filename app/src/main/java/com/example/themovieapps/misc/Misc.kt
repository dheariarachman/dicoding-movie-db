package com.example.themovieapps.misc

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object Misc {
    const val BASE_URL = "https://api.themoviedb.org/"
    const val BASE_IMG_URL = "https://image.tmdb.org/t/p/w185"

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @SuppressLint("SimpleDateFormat")
    fun convertStringToDate(textDate: String, firstPattern: String, secondPattern: String): String {
        val parser = SimpleDateFormat(firstPattern).parse(textDate)
        val formatter = SimpleDateFormat(secondPattern, Locale.getDefault())
        return formatter.format(parser)
    }
}