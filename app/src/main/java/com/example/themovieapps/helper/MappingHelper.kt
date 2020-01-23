package com.example.themovieapps.helper

import android.database.Cursor
import com.example.themovieapps.db.movie.FavoriteMovies.Companion.DESC
import com.example.themovieapps.db.movie.FavoriteMovies.Companion.IMG
import com.example.themovieapps.db.movie.FavoriteMovies.Companion.TITLE
import com.example.themovieapps.db.movie.FavoriteMovies.Companion.YEARS
import com.example.themovieapps.db.movie.FavoriteMovies.Companion._ID
import com.example.themovieapps.db.serial.FavoriteSerial
import com.example.themovieapps.model.Movie
import com.example.themovieapps.model.Serial

object MappingHelper {
    fun mapCursorToArray(movieCursor: Cursor?): ArrayList<Movie> {
        val movieList = ArrayList<Movie>()

        if (movieCursor != null) {
            if (movieCursor.moveToFirst()) {
                do {
                    val id =
                        movieCursor.getInt(movieCursor.getColumnIndexOrThrow(_ID))
                    val title =
                        movieCursor.getString(movieCursor.getColumnIndexOrThrow(TITLE))
                    val description =
                        movieCursor.getString(movieCursor.getColumnIndexOrThrow(DESC))
                    val imagePoster =
                        movieCursor.getString(movieCursor.getColumnIndexOrThrow(IMG))
                    val years =
                        movieCursor.getString(movieCursor.getColumnIndexOrThrow(YEARS))
                    movieList.add(Movie(id, title, description, imagePoster, years))
                } while (movieCursor.moveToNext())
            }
            movieCursor.close()
        }

        return movieList
    }

    fun mapCursorSerialToArray(movieCursor: Cursor?): ArrayList<Serial> {
        val serialList = ArrayList<Serial>()

        if (movieCursor != null) {
            if (movieCursor.moveToFirst()) {
                do {
                    val id =
                        movieCursor.getInt(movieCursor.getColumnIndexOrThrow(FavoriteSerial._ID))
                    val title =
                        movieCursor.getString(movieCursor.getColumnIndexOrThrow(FavoriteSerial.TITLE))
                    val description =
                        movieCursor.getString(movieCursor.getColumnIndexOrThrow(FavoriteSerial.DESC))
                    val imagePoster =
                        movieCursor.getString(movieCursor.getColumnIndexOrThrow(FavoriteSerial.IMG))
                    val years =
                        movieCursor.getString(movieCursor.getColumnIndexOrThrow(FavoriteSerial.YEARS))
                    serialList.add(Serial(id, title, description, imagePoster, years))
                } while (movieCursor.moveToNext())
            }
        }
        movieCursor?.close()
        return serialList
    }
}