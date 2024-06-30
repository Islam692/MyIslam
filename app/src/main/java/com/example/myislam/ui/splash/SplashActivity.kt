package com.example.myislam.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.example.myislam.R
import com.example.myislam.ui.home.HomeActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        adjustLayoutForSystemBars()
        startHomeActivity()
    }

    private fun adjustLayoutForSystemBars() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splash_main_layout)) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.updateLayoutParams<MarginLayoutParams> {
                bottomMargin = systemBarsInsets.bottom
            }

            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
            windowInsetsController.isAppearanceLightNavigationBars = false

            WindowInsetsCompat.CONSUMED
        }
    }

    // TODO: add more audios later.
    private fun startHomeActivity() {
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.quran)
        mediaPlayer.start()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 7000)
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}