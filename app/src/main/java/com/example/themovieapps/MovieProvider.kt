package com.example.themovieapps

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.themovieapps.db.DatabaseContract.AUTHORITY
import com.example.themovieapps.db.movie.FavoriteMovies.Companion.CONTENT_URI_MOVIE
import com.example.themovieapps.db.movie.FavoriteMovies.Companion.TABLE_NAME_MOVIE
import com.example.themovieapps.db.movie.MovieHelper
import com.example.themovieapps.db.serial.FavoriteSerial.Companion.TABLE_NAME_SERIAL
import com.example.themovieapps.db.serial.SerialHelper

class MovieProvider : ContentProvider() {

    companion object {
        private const val STATUS_VIEW = 1
        private const val STATUS_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var movieHelper: MovieHelper
        private lateinit var serialHelper: SerialHelper

        init {
            // Movie Content URI
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME_MOVIE, STATUS_VIEW)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME_MOVIE/#", STATUS_ID)

            // Serial Content URI
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME_SERIAL, STATUS_VIEW)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME_SERIAL/#", STATUS_ID)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return when (STATUS_ID) {
            sUriMatcher.match(uri) -> {
                when (uri.pathSegments[0]) {
                    TABLE_NAME_SERIAL -> serialHelper.deleteBy(uri.lastPathSegment.toString())
                    TABLE_NAME_MOVIE -> movieHelper.deleteBy(uri.lastPathSegment.toString())
                    else -> 0
                }
            }
            else -> 0
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val idItem = values?.get("_id")
        val added: Long = when (STATUS_VIEW) {
            sUriMatcher.match(uri) -> {
                when (uri.pathSegments[0]) {
                    TABLE_NAME_MOVIE -> {
                        if (!movieHelper.queryById(idItem as Int?)) {
                            movieHelper.insert(values)
                        } else {
                            0
                        }
                    }
                    TABLE_NAME_SERIAL -> {
                        if (!serialHelper.queryById(idItem as Int?)) {
                            serialHelper.insert(values)
                        } else {
                            0
                        }
                    }
                    else -> 0
                }
            }
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI_MOVIE, null)
        return Uri.parse("$CONTENT_URI_MOVIE/$added")
    }

    override fun onCreate(): Boolean {
        movieHelper = MovieHelper.getInstance(context as Context)
        serialHelper = SerialHelper.getInstance(context as Context)

        serialHelper.open()
        movieHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {

        return when (sUriMatcher.match(uri)) {
            STATUS_VIEW -> {
                when (uri.pathSegments[0]) {
                    TABLE_NAME_MOVIE -> movieHelper.queryAll()
                    TABLE_NAME_SERIAL -> serialHelper.queryAll()
                    else -> null
                }
            }
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}
