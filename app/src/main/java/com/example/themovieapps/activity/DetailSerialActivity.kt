package com.example.themovieapps.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.themovieapps.R
import com.example.themovieapps.model.Serial
import kotlinx.android.synthetic.main.activity_detail_serial.*
import java.text.SimpleDateFormat
import java.util.*

class DetailSerialActivity : AppCompatActivity() {

    companion object {
        const val DETAIL_SERIAL = "DETAIL_SERIAL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_serial)

        val detailSerial = intent.getParcelableExtra(DETAIL_SERIAL) as Serial
        loadDetailSerial(detailSerial)

    }

    @SuppressLint("SimpleDateFormat")
    private fun loadDetailSerial(serial: Serial) {
        val randomFav = (0..999).random()
        val randomComm = (0..99).random()
        Glide.with(this).load("https://image.tmdb.org/t/p/w185${serial.imgPoster}").fitCenter()
            .into(img_banner_detail_serial)
        tv_title_detail_serial.text = serial.title

        val parser = SimpleDateFormat("yyyy-MM-dd").parse(serial.years)
        val formatter = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        tv_release_date_detail_serial.text = formatter.format(parser)
        tv_description_detail_serial.text =
            if (serial.description.isNotEmpty()) serial.description else resources.getString(R.string.no_translations)
        tv_favorite_detail_serial.text = randomFav.toString()
        tv_comment_serial.text = randomComm.toString()
    }
}
