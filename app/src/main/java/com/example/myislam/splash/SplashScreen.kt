package com.example.myislam.splash

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.myislam.R
import com.example.myislam.home.HomeActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startHomeActivity()
    }

    private fun startHomeActivity() {
        var mediaplayer = MediaPlayer.create(applicationContext, R.raw.quran)
        mediaplayer.start()
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashScreen, HomeActivity::class.java))
            finish()
        }, 7000)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.colorr)
        }
    }
}