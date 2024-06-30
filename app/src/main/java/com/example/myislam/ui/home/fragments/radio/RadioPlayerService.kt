package com.example.myislam.ui.home.fragments.radio

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import com.example.myislam.R
import com.example.myislam.data.radio_api.api.ApiManager
import com.example.myislam.data.radio_api.models.Radio
import com.example.myislam.data.radio_api.models.RadioResponse
import com.example.myislam.ui.home.fragments.radio.NotificationRemoteViewHelper.setupClickActions
import com.example.myislam.ui.home.fragments.radio.NotificationRemoteViewHelper.showLoadingProgress
import com.example.myislam.ui.home.fragments.radio.NotificationRemoteViewHelper.showPauseButton
import com.example.myislam.ui.home.fragments.radio.NotificationRemoteViewHelper.showPlayButton
import com.example.myislam.ui.home.fragments.radio.NotificationRemoteViewHelper.showPlayPauseButton
import com.example.myislam.utils.Constants.CLOSE_ACTION
import com.example.myislam.utils.Constants.INIT_SERVICE
import com.example.myislam.utils.Constants.NEXT_ACTION
import com.example.myislam.utils.Constants.PLAY_ACTION
import com.example.myislam.utils.Constants.PREVIOUS_ACTION
import com.example.myislam.utils.Constants.RADIO_SERVICE_ID
import com.example.myislam.utils.Constants.START_ACTION
import com.example.myislam.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

// TODO: try stopping service and interact with the UI, it won't work. Solve this issue later.

const val LOGGING_TAG = "RadioService"

@AndroidEntryPoint
class RadioPlayerService : Service() {

    @Inject
    lateinit var notificationHelper: NotificationHelper
    private lateinit var notificationRemoteView: RemoteViews
    private lateinit var notification: Notification

    private var _mediaPlayer: MediaPlayer? = null
    private val mediaPlayer: MediaPlayer get() = _mediaPlayer!!
    private var isMediaPlayerAvailable = false
    private lateinit var radiosList: List<Radio>
    private var isPlaying = false
    private var currentRadioIndex = 0
    private var currentRadio: Radio = Radio()

    @Inject
    lateinit var utils: Utils

    inner class RadioPlayerBinder : Binder() {
        fun getService(): RadioPlayerService {
            return this@RadioPlayerService
        }
    }

    private val binder: IBinder = RadioPlayerBinder()

    override fun onBind(intent: Intent?): IBinder = binder


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d(LOGGING_TAG, "Radio service started")
        intent?.getIntExtra(START_ACTION, -1)?.let { action ->
            when (action) {
                INIT_SERVICE -> {
                    startForegroundServiceWithNotification() // in case started after stopped
                    loadRadios()
                }

                PLAY_ACTION -> playOrPauseRadio()
                NEXT_ACTION -> playNextRadio()
                PREVIOUS_ACTION -> playPreviousRadio()
                CLOSE_ACTION -> stopService()
                else -> Log.d(LOGGING_TAG, "Unknown start action with code $action")
            }
        }

        return START_STICKY
    }

    private fun stopService() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        radioPlayerCallback?.onServiceStopped()
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(LOGGING_TAG, "Radio service created")
        startForegroundServiceWithNotification()
    }

    private fun startForegroundServiceWithNotification() {
        try {
            notificationHelper.createNotificationChannel()
            notificationRemoteView = createNotificationRemoteView()
            notification = notificationHelper.createNotification(this, notificationRemoteView)
            notificationHelper.updateNotification(notification)
            startForeground(RADIO_SERVICE_ID, notification)
        } catch (e: Exception) {
            Log.d(LOGGING_TAG, "Error occurred: ${e.message}")
        }
    }

    private fun createNotificationRemoteView(): RemoteViews {
        RemoteViews(this.packageName, R.layout.notification_collapsed_content).apply {
            val defaultRadioTitle = resources.getString(R.string.radio_default_title)
            setTextViewText(R.id.notification_title, defaultRadioTitle)
            setupClickActions(this@RadioPlayerService)
            return this
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOGGING_TAG, "Radio service destroyed")
        mediaPlayer.release()
        _mediaPlayer = null
    }

    fun playOrPauseRadio() {
        if (isPlaying) {
            mediaPlayer.pause()
            isPlaying = false
            notificationRemoteView.showPlayButton()
            radioPlayerCallback?.onPaused(currentRadio)
        } else {
            if (!isMediaPlayerAvailable) {
                utils.showShortToast("Radio player not available, refreshing...")
                return
            }

            mediaPlayer.start()
            isPlaying = true
            notificationRemoteView.showPauseButton()
            radioPlayerCallback?.onPlayed(currentRadio)
        }

        notificationHelper.updateNotification(notification)
    }

    fun playPreviousRadio() {
        isMediaPlayerAvailable = false
        isPlaying = false
        updateNotificationForLoading()

        currentRadioIndex = if (currentRadioIndex == 0) radiosList.size - 1 else --currentRadioIndex
        playRadioAtCurrentIndex(false)
    }

    fun playNextRadio() {
        isMediaPlayerAvailable = false
        isPlaying = false
        updateNotificationForLoading()

        currentRadioIndex = if (currentRadioIndex == radiosList.size - 1) 0 else ++currentRadioIndex
        playRadioAtCurrentIndex(true)
    }

    private fun updateNotificationForLoading() {
        notificationRemoteView.showLoadingProgress()
        radioPlayerCallback?.onLoading()
        notificationHelper.updateNotification(notification)
    }

    private fun playRadioAtCurrentIndex(isPlayingNext: Boolean) {
        currentRadio = radiosList[currentRadioIndex]
        mediaPlayer.apply {
            reset()
            setDataSource(currentRadio.url)
            prepareAsync()
            setOnPreparedListener {
                isMediaPlayerAvailable = true
                notificationRemoteView.showPlayPauseButton()
                notificationRemoteView.setTextViewText(R.id.notification_title, currentRadio.name)

                start()
                this@RadioPlayerService.isPlaying = true
                notificationRemoteView.showPauseButton()
                notificationHelper.updateNotification(notification)

                if (isPlayingNext) radioPlayerCallback?.onNextPlayed(currentRadio)
                else radioPlayerCallback?.onPreviousPlayed(currentRadio)
            }
        }
    }


    private fun loadRadios() {
        notificationRemoteView.showLoadingProgress()
        notificationHelper.updateNotification(notification)

        ApiManager.getRadiosWebService()
            .getRadios(language = utils.getCurrentLanguageCodeForApi())
            .enqueue(object : Callback<RadioResponse> {
                override fun onResponse(
                    call: Call<RadioResponse>,
                    response: Response<RadioResponse>
                ) {
                    if (response.isSuccessful) {
                        radiosList = response.body()?.radios ?: emptyList()
                        currentRadio = radiosList[currentRadioIndex]
                        notificationRemoteView.showPlayPauseButton()
                        notificationHelper.updateNotification(notification)
                        if (_mediaPlayer == null) initializeMediaPlayer()
                    } else {
                        Log.d(
                            LOGGING_TAG,
                            "Radio service error: ${
                                response.errorBody()?.toString() ?: "Unknown error"
                            }"
                        )
                    }
                }

                override fun onFailure(call: Call<RadioResponse>, throwable: Throwable) {
                    Log.d(LOGGING_TAG, throwable.message ?: "Radio service unknown error")
                }
            })
    }

    private fun initializeMediaPlayer(name: String? = null, url: String? = null) {
        _mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url ?: currentRadio.url)
            prepareAsync()
            setOnPreparedListener {
                isMediaPlayerAvailable = true
                notificationRemoteView.showPlayPauseButton()
                notificationRemoteView.setTextViewText(
                    R.id.notification_title,
                    name ?: currentRadio.name
                )
                this@RadioPlayerService.isPlaying = true
                notificationRemoteView.showPauseButton()
                notificationHelper.updateNotification(notification)
                start()
                radioPlayerCallback?.onPlayed(currentRadio)
            }
        }
    }

    private var radioPlayerCallback: RadioPlayerCallback? = null

    fun setRadioPlayerCallback(callback: RadioPlayerCallback) {
        radioPlayerCallback = callback
    }

    interface RadioPlayerCallback {
        fun onPlayed(radio: Radio)
        fun onPaused(radio: Radio)
        fun onNextPlayed(radio: Radio)
        fun onPreviousPlayed(radio: Radio)
        fun onLoading()
        fun onServiceStopped()
    }
}
