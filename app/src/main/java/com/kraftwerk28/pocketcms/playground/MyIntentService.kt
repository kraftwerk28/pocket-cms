package com.kraftwerk28.pocketcms.playground

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kraftwerk28.pocketcms.R

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class MyIntentService : IntentService("Notifications") {
    private var _nID = 0
    private val notificationId
        get() = _nID++

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onHandleIntent(intent: Intent?) {

        val notificationMgr =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            createNotificationChannel(notificationMgr)
        }

        notify("My app", "Sample text")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(manager: NotificationManager) {
        val name = getString(R.string.channel_name)

        val chan = NotificationChannel(
            Settings.EXTRA_CHANNEL_ID,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager.createNotificationChannel(chan)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun notify(title: String, msg: String) {
        val builder = NotificationCompat.Builder(this, Settings.EXTRA_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(msg)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val comp = NotificationManagerCompat.from(this)
        comp.notify(notificationId, builder.build())
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("MyIntentService", "Service created.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MyIntentService", "Service destroyed.")
    }
}
