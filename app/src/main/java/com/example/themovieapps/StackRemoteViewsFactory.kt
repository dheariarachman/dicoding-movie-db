package com.example.themovieapps

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Binder
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.themovieapps.db.movie.FavoriteMovies
import com.example.themovieapps.db.movie.FavoriteMovies.Companion.CONTENT_URI_MOVIE
import com.example.themovieapps.db.movie.MovieHelper
import com.example.themovieapps.helper.MappingHelper
import com.example.themovieapps.misc.Misc
import com.example.themovieapps.model.Movie
import com.example.themovieapps.service.MovieDBService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Movie>()
    private var cursor: Cursor? = null
    private var movieHelper: MovieHelper? = null

    override fun onDataSetChanged() {

        if (cursor != null) {
            cursor?.close()
        }

        val identityToken = Binder.clearCallingIdentity()
        movieHelper = MovieHelper.getInstance(mContext)
        movieHelper?.open()
        cursor = movieHelper?.queryAll()!!
        mWidgetItems.addAll(MappingHelper.mapCursorToArray(cursor))
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun onCreate() {}

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item_movie)
        val bitmap =
            Glide.with(mContext).asBitmap().load(Misc.BASE_IMG_URL + mWidgetItems[position].imgPoster).submit(512, 512)
                .get()
        rv.setImageViewBitmap(R.id.widgetImageView, bitmap)

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