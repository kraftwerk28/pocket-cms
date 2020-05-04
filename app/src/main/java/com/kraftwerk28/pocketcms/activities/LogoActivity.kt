package com.kraftwerk28.pocketcms.activities

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.annotation.RequiresApi
import com.kraftwerk28.pocketcms.R

class LogoActivity : AppCompatActivity() {
    val showLogo = true
    @RequiresApi(21)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo)

        if (showLogo) {
            object : CountDownTimer(3000L, 1000L) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    val intent =
                        Intent(this@LogoActivity, DBActivity::class.java)
                    val options = ActivityOptions
                        .makeSceneTransitionAnimation(this@LogoActivity)
                        .toBundle()
                    startActivity(intent, options)
                }
            }.start()
        } else {
            val intent = Intent(this@LogoActivity, DBActivity::class.java)
            val options = ActivityOptions
                .makeSceneTransitionAnimation(this@LogoActivity)
                .toBundle()
            startActivity(intent, options)
        }

    }
}
