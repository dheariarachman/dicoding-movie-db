package com.example.themovieapps

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.themovieapps.db.movie.FavoriteMovies
import com.example.themovieapps.helper.MappingHelper
import com.example.themovieapps.misc.Misc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Bitmap>()

    override fun onDataSetChanged() {
        var cursor: Cursor? = null
        try {
            GlobalScope.launch(Dispatchers.Main) {
                val differedMovies = async(Dispatchers.IO) {
                    cursor = mContext.contentResolver.query(
                        FavoriteMovies.CONTENT_URI_MOVIE,
                        null,
                        null,
                        null,
                        null
                    )
                    MappingHelper.mapCursorToArray(cursor)
                }
                val movies = differedMovies.await()
                movies.forEach {
                    mWidgetItems.add(
//                        Glide.with(mContext).asBitmap().load(Misc.BASE_IMG_URL + it.imgPoster).get()
                    Glide.with(mContext).downloadOnly()
                        .diskCacheStrategy(DiskCacheStrategy.DATA) // Cache resource before it's decoded
                        .load(Misc.BASE_IMG_URL + it.imgPoster)
                        .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get()
                    )
                }
            }
        } finally {
            cursor?.close()
        }
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun onCreate() {}

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item_movie)
        rv.setImageViewBitmap(R.id.widgetImageView, mWidgetItems[position])

        val extra = bundleOf(
            FavoriteMovieWidget.EXTRA_ITEM to position
        )
        val fillIntent = Intent()
        fillIntent.putExtras(extra)

        rv.setOnClickFillInIntent(R.id.widgetImageView, fillIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {}

}