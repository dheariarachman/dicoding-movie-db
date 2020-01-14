package com.example.themovieapps.helper

import android.database.Cursor
import com.example.themovieapps.db.DatabaseContract
import com.example.themovieapps.model.Movie
import com.example.themovieapps.model.Serial

object MappingHelper {
    fun mapCursorToArray(movieCursor: Cursor): ArrayList<Movie> {
        val movieList = ArrayList<Movie>()

        movieCursor.moveToFirst()

        while (movieCursor.moveToNext()) {
            val id = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovies._ID))
            val title = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovies.TITLE))
            val description = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovies.DESC))
            val imagePoster = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovies.IMG))
            val years = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovies.YEARS))
            movieList.add(Movie(id, title, description, imagePoster, years))
        }

        return movieList
    }

    fun mapCursorSerialToArray(movieCursor: Cursor): ArrayList<Serial> {
        val serialList = ArrayList<Serial>()

        movieCursor.moveToFirst()

        while (movieCursor.moveToNext()) {
            val id = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovies._ID))
            val title = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovies.TITLE))
            val description = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovies.DESC))
            val imagePoster = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovies.IMG))
            val years = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovies.YEARS))
            serialList.add(Serial(id, title, description, imagePoster, years))
        }

        return serialList
    }
}