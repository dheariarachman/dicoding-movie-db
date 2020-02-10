package com.example.themovieapps

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.themovieapps.misc.Misc.isDateInvalid
import com.example.themovieapps.model.MovieResponse
import com.example.themovieapps.service.MovieDBService
import com.example.themovieapps.service.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CatalogueReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE_RELEASE = "ReleaseNotification"
        const val TYPE_REPEAT = "RepeatNotification"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"

        private const val ID_RELEASE = 100
        private const val ID_REPEAT = 101

        private const val TIME_FORMAT = "HH:mm:ss"
        private const val DATE_FORMAT = "yyyy-MM-dd"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val type = intent?.getStringExtra(EXTRA_TYPE)
        val message = intent?.getStringExtra(EXTRA_MESSAGE)

        val notifyId = if (type.equals(TYPE_RELEASE, ignoreCase = true)) ID_RELEASE else ID_REPEAT

        showNotification(
            context,
            context?.resources?.getString(R.string.app_name),
            message,
            notifyId
        )
    }

    fun setRepeatingNotification(context: Context?, type: String, time: String, message: String) {
        if (isDateInvalid(time, TIME_FORMAT)) return

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, CatalogueReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)

        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, Integer.parseInt(timeArray[2]))

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEAT, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Toast.makeText(
            context,
            context?.resources?.getString(R.string.notificaton_configured),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun setReleasedMovieNotification(
        context: Context?,
        type: String,
        time: String,
        message: String
    ) {
        if (isDateInvalid(time, TIME_FORMAT)) return

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, CatalogueReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)

        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, Integer.parseInt(timeArray[2]))

        val pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Toast.makeText(
            context,
            context?.resources?.getString(R.string.notificaton_configured),
            Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * @param context
     * @param title
     * @param message
     * @param notifyId
     *
     * @return void
     */
    private fun showNotification(
        context: Context?,
        title: String?,
        message: String?,
        notifyId: Int?
    ) {
        val NOTIFICATION_ID = notifyId
        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "AlarmManager channel"
        var notificationManagerCompat: NotificationManager?
        var mBuilderNotification: NotificationCompat.Builder? = null

        if (NOTIFICATION_ID == ID_RELEASE) {
            notificationManagerCompat =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = sdf.format(Date())

            if (isDateInvalid(currentDate, DATE_FORMAT)) return

            val mApiService: MovieDBService? =
                RetrofitClient.client?.create(MovieDBService::class.java)
            val call =
                mApiService?.getReleasedMovieToday(BuildConfig.API_KEY, currentDate, currentDate)
            call?.enqueue(object : Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if (response.code() == 200) {
                        response.body()?.results?.forEach {
                            mBuilderNotification = NotificationCompat.Builder(context, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_notifications_white_24dp)
                                .setLargeIcon(
                                    BitmapFactory.decodeResource(
                                        context.resources,
                                        R.drawable.ic_notifications_white_24dp
                                    )
                                )
                                .setContentTitle(it.title)
                                .setContentText(it.overview)
                                .setAutoCancel(true)
                        }
                    }
                }

            })
        } else {
            notificationManagerCompat =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mBuilderNotification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_white_24dp)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.ic_notifications_white_24dp
                    )
                )
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_NAME
            mBuilderNotification?.setChannelId(CHANNEL_ID)
            notificationManagerCompat.run {
                channel.description = CHANNEL_NAME
                mBuilderNotification?.setChannelId(CHANNEL_ID)
                createNotificationChannel(channel)
            }
        }

        val notification = mBuilderNotification?.build()

        NOTIFICATION_ID?.let { notificationManagerCompat.run { notify(it, notification) } }
    }

    fun cancelNotificationRepeat(context: Context?, type: String) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, CatalogueReceiver::class.java)
        val requestCode =
            if (type.equals(TYPE_RELEASE, ignoreCase = true)) ID_RELEASE else ID_REPEAT
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(
            context,
            context.resources.getString(R.string.notificaton_canceled),
            Toast.LENGTH_SHORT
        ).show()
    }
}
