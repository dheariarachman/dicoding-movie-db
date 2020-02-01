package com.example.themovieapps.service

import android.content.Intent
import android.widget.RemoteViewsService
import com.example.themovieapps.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)

}