package com.kraftwerk28.pocketcms.playground

import android.content.Context
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import com.kraftwerk28.pocketcms.R
import kotlinx.android.synthetic.main.activity_charger_meter.*

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
