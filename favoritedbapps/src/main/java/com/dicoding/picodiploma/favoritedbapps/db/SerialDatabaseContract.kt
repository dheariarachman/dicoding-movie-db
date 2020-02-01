package com.dicoding.picodiploma.favoritedbapps.db

import android.net.Uri
import android.provider.BaseColumns

object SerialDatabaseContract {

    class FavoriteSerial : BaseColumns {
        companion object {
            const val TABLE_NAME_SERIAL = "serial_favorite"
            const val _ID = "_id"
            const val TITLE = "title"
            const val DESC = "description"
            const val IMG = "imgPoster"
            const val YEARS = "years"

            val CONTENT_URI_SERIAL: Uri =
                Uri.Builder().scheme(DatabaseContract.SCHEME).authority(DatabaseContract.AUTHORITY)
                    .appendPath(
                        TABLE_NAME_SERIAL
                    ).build()
        }
    }
}