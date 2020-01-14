package com.example.themovieapps.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.themovieapps.db.DatabaseContract.FavoriteMovies.Companion.TABLE_NAME
import com.example.themovieapps.db.DatabaseContract.FavoriteMovies.Companion._ID
import java.sql.SQLException

class MovieHelper(context: Context?) : QueryFavorite {
    private lateinit var database: SQLiteDatabase
    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: MovieHelper? = null

        fun getInstance(context: Context?): MovieHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = MovieHelper(context)
                    }
                }
            }
            return INSTANCE as MovieHelper
        }
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    override fun queryAll(): Cursor {
        return database.query(DATABASE_TABLE, null, null, null, null, null, "$_ID ASC", null)
    }

    override fun insert(values: ContentValues): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    override fun deleteBy(id: String?): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }
}
