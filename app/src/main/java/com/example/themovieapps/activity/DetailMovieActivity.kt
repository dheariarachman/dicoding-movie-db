package com.example.themovieapps.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.themovieapps.R
import com.example.themovieapps.misc.Misc.convertStringToDate
import com.example.themovieapps.model.Movie
import kotlinx.android.synthetic.main.activity_detail_movie.*

class DetailMovieActivity : AppCompatActivity() {

    companion object {
        const val DETAIL_MOVIE = "detail_movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        val detailMovie = intent.getParcelableExtra(DETAIL_MOVIE) as Movie
        loadDetailMovie(detailMovie)
    }

    @SuppressLint("SimpleDateFormat")
    private fun loadDetailMovie(detailMovie: Movie) {
        val randomFav = (0..999).random()
        val randomComm = (0..99).random()
        Glide.with(this).load("https://image.tmdb.org/t/p/w185${detailMovie.imgPoster}")
            .fitCenter().into(img_banner_detail)
        tv_title_detail.text = detailMovie.title

        /**
         * parsing date into SimpleDateFormat
         */
        tv_release_date_detail.text =
            convertStringToDate(detailMovie.years, "yyyy-MM-dd", "MMMM d, yyyy")

        tv_description_detail.text =
            detailMovie.description
                ?: resources.getString(
                    R.string.no_translations
                )
        tv_favorite_detail.text = randomFav.toString()
        tv_comment.text = randomComm.toString()
    }
}
