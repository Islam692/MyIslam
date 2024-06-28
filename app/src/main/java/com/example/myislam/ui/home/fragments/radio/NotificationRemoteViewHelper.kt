package com.example.myislam.ui.home.fragments.radio

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.example.myislam.R
import com.example.myislam.utils.Constants.CLOSE_ACTION
import com.example.myislam.utils.Constants.NEXT_ACTION
import com.example.myislam.utils.Constants.PLAY_ACTION
import com.example.myislam.utils.Constants.PREVIOUS_ACTION
import com.example.myislam.utils.Constants.START_ACTION

/**
 * A helper class to simplify the process of updating and interacting with RemoteViews,
 * which are used to define the appearance of notifications.
 *
 */
object NotificationRemoteViewHelper {

    /**
     * Sets a click listener on a view within a RemoteViews object to trigger an action
     * when the view is clicked.
     *
     * @param context The context in which the PendingIntent will be used.
     * @param viewId The ID of the view within the RemoteViews object.
     * @param actionCode An integer code representing the action to be performed.
     * @receiver The RemoteViews object to which the click listener is being added.
     */
    private fun RemoteViews.setOnClickActionCode(
        context: Context,
        viewId: Int,
        actionCode: Int
    ) {
        val actionIntent = Intent(context, RadioPlayerService::class.java)
        actionIntent.putExtra(START_ACTION, actionCode)

        val actionPendingIntent = PendingIntent
            .getService(context, actionCode, actionIntent, PendingIntent.FLAG_IMMUTABLE)

        this.setOnClickPendingIntent(viewId, actionPendingIntent)
    }

    fun RemoteViews.setupClickActions(context: Context) {
        this.setOnClickActionCode(context, R.id.notification_play, PLAY_ACTION)
        this.setOnClickActionCode(context, R.id.notification_next, NEXT_ACTION)
        this.setOnClickActionCode(context, R.id.notification_previous, PREVIOUS_ACTION)
        this.setOnClickActionCode(context, R.id.notification_close, CLOSE_ACTION)
    }

    fun RemoteViews.showLoadingProgress() {
        this.setViewVisibility(R.id.notification_play, View.GONE)
        this.setViewVisibility(R.id.notification_loading_progress, View.VISIBLE)
    }

    fun RemoteViews.showPlayPauseButton() {
        this.setViewVisibility(R.id.notification_play, View.VISIBLE)
        this.setViewVisibility(R.id.notification_loading_progress, View.GONE)
    }

    fun RemoteViews.showPlayButton() {
        this.setImageViewResource(R.id.notification_play, R.drawable.baseline_play_arrow_24)
    }

    fun RemoteViews.showPauseButton() {
        this.setImageViewResource(R.id.notification_play, R.drawable.ic_pause)
    }
}
