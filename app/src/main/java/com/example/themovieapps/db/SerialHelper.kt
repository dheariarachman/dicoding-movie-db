package com.example.themovieapps.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.themovieapps.db.DatabaseContract.FavoriteSerial.Companion.TABLE_NAME_SERIAL

class SerialHelper(context: Context?) : QueryFavorite {
    private lateinit var database: SQLiteDatabase
    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME_SERIAL
        private var INSTANCE: SerialHelper? = null

        fun getInstance(context: Context?): SerialHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = SerialHelper(context)
                    }
                }
            }
            return INSTANCE as SerialHelper
        }

    }

    @Throws(SQLiteException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        if (database.isOpen) {
            database.close()
            databaseHelper.close()
        }
    }

    override fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "${DatabaseContract.FavoriteSerial._ID} ASC",
            null
        )
    }

    override fun insert(values: ContentValues): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    override fun deleteBy(id: String?): Int {
        return database.delete(
            DATABASE_TABLE,
            "${DatabaseContract.FavoriteSerial._ID} = '$id'",
            null
        )
    }

    override fun countAll(): Long {
        return 0
    }
}