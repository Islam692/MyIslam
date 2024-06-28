package com.example.myislam.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.myislam.R
import com.example.myislam.ui.home.HomeActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startHomeActivity()
    }

    private fun startHomeActivity() {
//        val mediaplayer = MediaPlayer.create(applicationContext, R.raw.quran)
//        mediaplayer.start()
//        Handler(Looper.getMainLooper()).postDelayed({
        val intent = Intent(this@SplashScreen, HomeActivity::class.java)
        startActivity(intent)
        finish()
//        }, 7000)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.black)
    }
}