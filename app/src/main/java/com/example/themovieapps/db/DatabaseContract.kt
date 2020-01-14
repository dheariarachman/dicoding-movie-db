package com.example.themovieapps.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.themovieapps.db.DatabaseContract.FavoriteMovies.Companion.TABLE_NAME

internal class DatabaseContract {
    internal class FavoriteMovies : BaseColumns {
        companion object {
            const val TABLE_NAME = "movie_favorite"
            const val _ID = "_id"
            const val TITLE = "title"
            const val DESC = "description"
            const val IMG = "imgPoster"
            const val YEARS = "years"
        }
    }

    internal class FavoriteSerial : BaseColumns {
        companion object {
            const val TABLE_NAME = "serial_favorite"
            const val _ID = "_id"
            const val TITLE = "title"
            const val DESC = "description"
            const val IMG = "imgPoster"
            const val YEARS = "years"
        }
    }
}

internal class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "db_favorite"

        private const val DATABASE_VERSION = 2

        private const val SQL_CREATE_TABLE_MOVIE =
            "CREATE TABLE $TABLE_NAME" +
                    "(${DatabaseContract.FavoriteMovies._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${DatabaseContract.FavoriteMovies.TITLE} TEXT NOT NULL, " +
                    "${DatabaseContract.FavoriteMovies.DESC} TEXT NOT NULL, " +
                    "${DatabaseContract.FavoriteMovies.IMG} TEXT NOT NULL, " +
                    "${DatabaseContract.FavoriteMovies.YEARS} TEXT NOT NULL )"

        private const val SQL_DROP_TABLE_MOVIE = "DROP TABLE IF EXISTS $TABLE_NAME"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_MOVIE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DROP_TABLE_MOVIE)
        onCreate(db)
    }
}