package com.example.myislam.player

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.myislam.MyApplication
import com.example.myislam.R


class PlayService : Service() {
    val binder = MyBinder()
    var mediaPlayer = MediaPlayer()

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    inner class MyBinder : Binder()

    fun getService(): PlayService {
        return this@PlayService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
        val urlToPlay = intent?.getStringExtra("url")
        val name = intent?.getStringExtra("name")
        val action = intent?.getStringExtra("action")

        if (urlToPlay != null && name != null)
            startMediaPlayer(urlToPlay, name)


        if (action != null)
            Log.e("action", action)

        if (action.equals("play")) {
            playPouseMediaPlayer()
        } else if (action.equals("stop")) {
            stopMediaPlayer()
        }
        return START_NOT_STICKY
    }

    var name: String = ""
    private fun startMediaPlayer(urlToPlay: String, name: String) {
        pauseMediaPlayer()
        this.name = name
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(this, Uri.parse(urlToPlay))
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            it.start()
        }
        createNotifacationForMediaPlayer(name)
    }

    private fun playPouseMediaPlayer() {
        Log.e("action", "playPause")
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
        updateNotofacation()
    }

    private fun stopMediaPlayer() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.reset()
        }
        stopForeground(true)
        stopSelf()
    }

    private fun pauseMediaPlayer() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    private fun createNotifacationForMediaPlayer(name: String) {
        val defultView = RemoteViews(packageName, R.layout.notificationview)
        defultView.setTextViewText(R.id.title, "My Islam App Radio")
        defultView.setTextViewText(R.id.description, name)
        defultView.setImageViewResource(
            R.id.play,
            if (mediaPlayer.isPlaying) R.drawable.ic_stop else R.drawable.baseline_play_arrow_24
        )
        defultView.setOnClickPendingIntent(R.id.play, getPlayButtonPendingIntent())
        defultView.setOnClickPendingIntent(R.id.stop, getStopButtonPendingIntent())

        var builder =
            NotificationCompat.Builder(this, MyApplication.RADIO_PLAYER_NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_notification)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(defultView)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSound(null)
        startForeground(20, builder.build())
    }

    private fun updateNotofacation() {
        val defaultView = RemoteViews(packageName, R.layout.notificationview)
        defaultView.setTextViewText(R.id.title, "My Islam App Radio")
        defaultView.setTextViewText(R.id.description, name)
        defaultView.setImageViewResource(
            R.id.play,
            if (mediaPlayer.isPlaying) R.drawable.ic_stop else R.drawable.baseline_play_arrow_24
        )
        defaultView.setOnClickPendingIntent(R.id.play, getPlayButtonPendingIntent())
        defaultView.setOnClickPendingIntent(R.id.stop, getStopButtonPendingIntent())

        var builder =
            NotificationCompat.Builder(this, MyApplication.RADIO_PLAYER_NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_notification)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(defaultView)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setDefaults(0)
                .setSound(null)
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(20, builder.build())
    }

    val PLAY_ACTION = "PLAY"
    val STOP_ACTION = "STOP"
    private fun getPlayButtonPendingIntent(): PendingIntent? {
        val intent = Intent(this, PlayService::class.java)
        intent.putExtra("action", PLAY_ACTION)
        val pendingIntent = PendingIntent.getService(
            this, 12, intent, PendingIntent.FLAG_IMMUTABLE
        )
        return pendingIntent
    }

    private fun getStopButtonPendingIntent(): PendingIntent? {
        val intent = Intent(this, PlayService::class.java)
        intent.putExtra("action", STOP_ACTION)
        val pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        return pendingIntent
    }

}