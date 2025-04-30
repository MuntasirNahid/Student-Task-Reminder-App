package com.example.student_task_reminder_app.service.FocusMode

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.example.student_task_reminder_app.R

class FocusModeService : Service() {

    ///This lazily initializes a reference to the system’s NotificationManager, used to post or update notifications.
    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    //Unique ID for your notification channel (used on Android 8+).
    private val channelId = "focus_mode_channel"

    //ID to identify and update the notification if needed.
    private val notificationId = 1


    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //Puts the service in the foreground with a persistent notification (can't be swiped away easily).
        startForeground(notificationId, createNotification())
        // If the system kills the service, it should be restarted automatically with a null intent.
        return START_STICKY
    }

    //Returns null because this is a started service, not a bound service (i.e., no clients are binding to it).
    override fun onBind(intent: Intent?): IBinder? = null


    private fun createNotificationChannel() {
        // Creates a notification channel for Android 8.0 (API level 26) and above.As of Android 13, channels are mandatory.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Focus Mode",
                NotificationManager.IMPORTANCE_LOW //Means it won’t make sound or appear prominently.
            ).apply {
                description = "Focus Mode is Active" //Shown in Android's notification settings.
                setShowBadge(false) // Hides app icon badge for this channel.
            }
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun createNotification(): Notification {
        // Creates an explicit intent to open FocusModeActivity when the user taps the notification or stop action.
        val stopIntent = Intent(this, FocusModeActivity::class.java).apply {
            // Flags ensure it opens as a fresh task.
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // Wraps the intent in a PendingIntent, which allows the notification to launch the activity securely.
        val stopPendingIntent = PendingIntent.getActivity(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Creates the notification using NotificationCompat.Builder, which is compatible with older Android versions.
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Focus Mode Active")
            .setContentText("Tap to stop Focus Mode")
            .setSmallIcon(R.drawable.ic_focus)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .addAction(
                R.drawable.ic_stop,
                "Stop",
                stopPendingIntent
            )
            .build()


    }
}