package com.example.themovieapps.db.movie

import android.net.Uri
import android.provider.BaseColumns
import com.example.themovieapps.db.DatabaseContract

internal class FavoriteMovies : BaseColumns {
    companion object {
        const val TABLE_NAME_MOVIE = "movie_favorite"
        const val _ID = "_id"
        const val TITLE = "title"
        const val DESC = "description"
        const val IMG = "imgPoster"
        const val YEARS = "years"

        val CONTENT_URI_MOVIE: Uri =
            Uri.Builder().scheme(DatabaseContract.SCHEME).authority(DatabaseContract.AUTHORITY)
                .appendPath(
                    TABLE_NAME_MOVIE
                ).build()
    }
}