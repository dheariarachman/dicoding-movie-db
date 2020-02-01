package com.example.themovieapps

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Bitmap>()

    override fun onDataSetChanged() {}

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