package com.example.myislam.splash

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
            val intent = Intent(this@SplashScreen, HomeActivity::class.java)
            startActivity(intent)
        },7000)
    }
}