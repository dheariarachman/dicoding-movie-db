package com.example.themovieapps.misc

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object Misc {
    const val BASE_URL = "https://api.themoviedb.org/"
    const val BASE_IMG_URL = "https://image.tmdb.org/t/p/w185"

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @SuppressLint("SimpleDateFormat")
    fun convertStringToDate(
        textDate: String?,
        firstPattern: String,
        secondPattern: String
    ): String {
        var date = ""
        if (textDate != "") {
            val parser = SimpleDateFormat(firstPattern).parse(textDate)
            val formatter = SimpleDateFormat(secondPattern, Locale.getDefault())
            date = formatter.format(parser)
        }
        return date
    }

    fun isDateInvalid(date: String, format: String) : Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: Exception) {
            true
        }
    }
}