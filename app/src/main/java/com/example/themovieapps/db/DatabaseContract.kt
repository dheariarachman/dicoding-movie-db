package com.example.themovieapps.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.themovieapps.db.DatabaseContract.FavoriteMovies.Companion.TABLE_NAME_MOVIE
import com.example.themovieapps.db.DatabaseContract.FavoriteSerial.Companion.TABLE_NAME_SERIAL

internal class DatabaseContract {
    internal class FavoriteMovies : BaseColumns {
        companion object {
            const val TABLE_NAME_MOVIE = "movie_favorite"
            const val _ID = "_id"
            const val TITLE = "title"
            const val DESC = "description"
            const val IMG = "imgPoster"
            const val YEARS = "years"
        }
    }

    internal class FavoriteSerial : BaseColumns {
        companion object {
            const val TABLE_NAME_SERIAL = "serial_favorite"
            const val _ID = "_id"
            const val TITLE = "title"
            const val DESC = "description"
            const val IMG = "imgPoster"
            const val YEARS = "years"
        }
    }
}

internal class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "db_favorite"

        private const val DATABASE_VERSION = 3

        private const val SQL_CREATE_TABLE_MOVIE =
            "CREATE TABLE $TABLE_NAME_MOVIE" +
                    "(${DatabaseContract.FavoriteMovies._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${DatabaseContract.FavoriteMovies.TITLE} TEXT NOT NULL, " +
                    "${DatabaseContract.FavoriteMovies.DESC} TEXT NOT NULL, " +
                    "${DatabaseContract.FavoriteMovies.IMG} TEXT NOT NULL, " +
                    "${DatabaseContract.FavoriteMovies.YEARS} TEXT NOT NULL )"

        private const val SQL_DROP_TABLE_MOVIE = "DROP TABLE IF EXISTS $TABLE_NAME_MOVIE"

        private const val SQL_CREATE_TABLE_SERIAL =
            "CREATE TABLE $TABLE_NAME_SERIAL" +
                    "(${DatabaseContract.FavoriteMovies._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${DatabaseContract.FavoriteMovies.TITLE} TEXT NOT NULL, " +
                    "${DatabaseContract.FavoriteMovies.DESC} TEXT NOT NULL, " +
                    "${DatabaseContract.FavoriteMovies.IMG} TEXT NOT NULL, " +
                    "${DatabaseContract.FavoriteMovies.YEARS} TEXT NOT NULL )"

        private const val SQL_DROP_TABLE_SERIAL = "DROP TABLE IF EXISTS $TABLE_NAME_SERIAL"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_MOVIE)
        db?.execSQL(SQL_CREATE_TABLE_SERIAL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DROP_TABLE_MOVIE)
        db?.execSQL(SQL_DROP_TABLE_SERIAL)
        onCreate(db)
    }
}