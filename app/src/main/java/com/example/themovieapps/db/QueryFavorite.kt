package com.example.themovieapps.db

import android.content.ContentValues
import android.database.Cursor

interface QueryFavorite {
    fun queryAll(): Cursor
    fun insert(values: ContentValues): Long
    fun deleteBy(id: String?):Int
}