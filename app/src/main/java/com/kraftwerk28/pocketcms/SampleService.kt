package com.kraftwerk28.pocketcms

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class SampleService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.d("Sample service", "Service created.")
        Thread.sleep(10000L)
        stopSelf()
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Sample service", "Service destroyed.")
    }
}
