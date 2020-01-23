package com.example.themovieapps.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.themovieapps.db.movie.FavoriteMovies
import com.example.themovieapps.db.movie.FavoriteMovies.Companion.TABLE_NAME_MOVIE
import com.example.themovieapps.db.serial.FavoriteSerial
import com.example.themovieapps.db.serial.FavoriteSerial.Companion.TABLE_NAME_SERIAL

object DatabaseContract {

    const val AUTHORITY = "com.example.themovieapps"
    const val SCHEME = "content"
}

internal class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "db_favorite"

        private const val DATABASE_VERSION = 6

        private const val SQL_CREATE_TABLE_MOVIE =
            "CREATE TABLE $TABLE_NAME_MOVIE" +
                    "(${FavoriteMovies._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${FavoriteMovies.TITLE} TEXT NOT NULL, " +
                    "${FavoriteMovies.DESC} TEXT NOT NULL, " +
                    "${FavoriteMovies.IMG} TEXT NOT NULL, " +
                    "${FavoriteMovies.YEARS} TEXT NOT NULL )"

        private const val SQL_DROP_TABLE_MOVIE = "DROP TABLE IF EXISTS $TABLE_NAME_MOVIE"

        private const val SQL_CREATE_TABLE_SERIAL =
            "CREATE TABLE $TABLE_NAME_SERIAL" +
                    "(${FavoriteSerial._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${FavoriteSerial.TITLE} TEXT NOT NULL, " +
                    "${FavoriteSerial.DESC} TEXT NOT NULL, " +
                    "${FavoriteSerial.IMG} TEXT NOT NULL, " +
                    "${FavoriteSerial.YEARS} TEXT NOT NULL )"

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