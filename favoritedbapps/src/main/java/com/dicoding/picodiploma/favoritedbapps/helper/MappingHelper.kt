package com.dicoding.picodiploma.favoritedbapps.helper

import android.database.Cursor
import com.dicoding.picodiploma.favoritedbapps.db.MovieDatabaseContract.FavoriteMovies.Companion.DESC
import com.dicoding.picodiploma.favoritedbapps.db.MovieDatabaseContract.FavoriteMovies.Companion.IMG
import com.dicoding.picodiploma.favoritedbapps.db.MovieDatabaseContract.FavoriteMovies.Companion.TITLE
import com.dicoding.picodiploma.favoritedbapps.db.MovieDatabaseContract.FavoriteMovies.Companion.YEARS
import com.dicoding.picodiploma.favoritedbapps.db.MovieDatabaseContract.FavoriteMovies.Companion._ID
import com.dicoding.picodiploma.favoritedbapps.db.SerialDatabaseContract
import com.dicoding.picodiploma.favoritedbapps.model.Movie
import com.dicoding.picodiploma.favoritedbapps.model.Serial

object MappingHelper {

    fun mappingMovieCursorToArray(movieCursor: Cursor?): ArrayList<Movie> {
        val arrayMovieList = ArrayList<Movie>()

        if (movieCursor != null) {
            if (movieCursor.moveToFirst()) {
                do {
                    val id = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(_ID))
                    val title = movieCursor.getString(movieCursor.getColumnIndexOrThrow(TITLE))
                    val description = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DESC))
                    val date = movieCursor.getString(movieCursor.getColumnIndexOrThrow(YEARS))
                    val poster = movieCursor.getString(movieCursor.getColumnIndexOrThrow(IMG))
                    arrayMovieList.add(Movie(id, title, date, poster, description))
                } while (movieCursor.moveToNext())
            }
        }

        return arrayMovieList
    }

    fun mappingSerialCursorToArray(serialCursor: Cursor?): ArrayList<Serial> {
        val arraySerialList = ArrayList<Serial>()

        if (serialCursor != null) {
            if (serialCursor.moveToFirst()) {
                do {
                    val id = serialCursor.getInt(
                        serialCursor.getColumnIndexOrThrow(SerialDatabaseContract.FavoriteSerial._ID)
                    )
                    val title = serialCursor.getString(
                        serialCursor.getColumnIndexOrThrow(SerialDatabaseContract.FavoriteSerial.TITLE)
                    )
                    val description = serialCursor.getString(
                        serialCursor.getColumnIndexOrThrow(SerialDatabaseContract.FavoriteSerial.DESC)
                    )
                    val releaseDate = serialCursor.getString(
                        serialCursor.getColumnIndexOrThrow(SerialDatabaseContract.FavoriteSerial.YEARS)
                    )
                    val poster = serialCursor.getString(
                        serialCursor.getColumnIndexOrThrow(SerialDatabaseContract.FavoriteSerial.IMG)
                    )
                    arraySerialList.add(Serial(id, title, description, releaseDate, poster))
                } while (serialCursor.moveToNext())
            }
        }

        return arraySerialList
    }

}