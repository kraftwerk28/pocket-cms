package com.kraftwerk28.pocketcms

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.ColorDrawable
import android.os.BatteryManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.kraftwerk28.pocketcms.playground.GreetingActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val logTag = "Main activity"
    val NOTIFICATOIN_CHAN_ID = "notif_chan_1"
    val NOTIFICATION_CHAN_NAME = "notif_chan_name"
    val actionReciver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(
                "Battery level",
                intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0).toString() + "%"
            )
            Log.d(
                "Screen state",
                "Screen unlocked"
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        intentFilter.addAction(Intent.ACTION_SCREEN_ON)
        registerReceiver(actionReciver, intentFilter)

        setBarCl(switch1.isChecked)
        switch1.setOnCheckedChangeListener { _, isChecked ->
            setBarCl(isChecked)
        }

        notbtn.setOnClickListener { sendNotification("Pocket CMS", editText.text.toString()) }
        editText.setOnEditorActionListener { v, actionId, event ->
            event.let {
                sendNotification("Pocket CMS", v.text.toString())
                true
            }
            false
        }

        nextActivityButton.setOnClickListener {
            val GAIntent = Intent(this@MainActivity, GreetingActivity::class.java)
            GAIntent.putExtra("title", editText.text.toString())
            startActivity(GAIntent)
        }


        createNotificationChannel()
    }

    fun logBundle(b: Bundle) = b.keySet().reduce { acc: String, s -> "$acc; $s: ${b.get(s)}" }

    fun sendNotification(title: String, text: String?) {
        val notification = NotificationCompat.Builder(this, NOTIFICATOIN_CHAN_ID)
            .setSmallIcon(R.drawable.ic_cloud_done_black_24dp)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val mgr = NotificationManagerCompat.from(this)
        mgr.notify(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                NOTIFICATOIN_CHAN_ID,
                NOTIFICATION_CHAN_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val mgr = getSystemService(NotificationManager::class.java)
            mgr?.createNotificationChannel(chan)
        }
    }

    private fun setBarCl(checked: Boolean) {
        val cl = ContextCompat.getColor(
            this,
            if (checked) R.color.primary else R.color.primary_dark
        )

        @RequiresApi(21)
        window.statusBarColor = cl
        supportActionBar?.setBackgroundDrawable(ColorDrawable(cl))
    }

    override fun onStart() {
        super.onStart()
        Log.i(logTag, "activity started.")
    }

    override fun onStop() {
        super.onStop()
        Log.i(logTag, "activity stopped.")
    }

    override fun onResume() {
        super.onResume()
        Log.i(logTag, "activity resumed.")
    }

    override fun onPause() {
        super.onPause()
        Log.i(logTag, "activity paused.")
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(actionReciver)

        Log.i(logTag, "activity destroyed.")
    }

}
