package com.example.themovieapps.db.movie

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.themovieapps.db.DatabaseHelper
import com.example.themovieapps.db.QueryFavorite
import com.example.themovieapps.db.movie.FavoriteMovies.Companion.TABLE_NAME_MOVIE
import com.example.themovieapps.db.movie.FavoriteMovies.Companion._ID
import java.sql.SQLException


class MovieHelper(context: Context?) :
    QueryFavorite {
    private lateinit var database: SQLiteDatabase
    private val databaseHelper: DatabaseHelper =
        DatabaseHelper(context)

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME_MOVIE
        private var INSTANCE: MovieHelper? = null

        fun getInstance(context: Context?): MovieHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            MovieHelper(context)
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

    override fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    override fun deleteBy(id: String?): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }

    override fun countAll(): Long {
        val sqlState = database.compileStatement("SELECT count(*) FROM $DATABASE_TABLE")
        return sqlState.simpleQueryForLong()
    }

    override fun queryById(id: Int?): Boolean{
        val idStr = id.toString()
        val limit = "1"
        val selection = "$_ID =?"
        val selectionArgs = arrayOf(idStr)

        val cursor = database.query(DATABASE_TABLE, null, selection, selectionArgs, null, null, null, limit)
        val exists = (cursor.count > 0)
        cursor.close()
        return exists

    }
}
