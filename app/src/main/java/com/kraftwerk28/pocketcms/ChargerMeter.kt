package com.kraftwerk28.pocketcms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_charger_meter.*
import kotlinx.coroutines.handleCoroutineException

class ChargerMeter : AppCompatActivity() {

    lateinit var runnable: Runnable
    val handler = Handler()
    lateinit var batteryMgr: BatteryManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charger_meter)
        batteryMgr = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    }

    @RequiresApi(21)
    override fun onStart() {
        super.onStart()
        runnable = Runnable {
            current_value.text =
                "Струм: ${batteryMgr.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)}"
            handler.postDelayed(runnable, 100L)
        }
        handler.postDelayed(runnable, 100L)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
    }
}
