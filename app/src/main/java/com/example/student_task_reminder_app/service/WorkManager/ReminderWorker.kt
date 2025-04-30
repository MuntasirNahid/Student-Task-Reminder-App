package com.example.student_task_reminder_app.service.WorkManager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.student_task_reminder_app.MainActivity
import com.example.student_task_reminder_app.R
import com.example.student_task_reminder_app.data.database.ReminderDatabase
import com.example.student_task_reminder_app.data.entity.Reminder
import java.util.concurrent.TimeUnit

/**
 * This class runs periodically in the background (even if the app is not open), using WorkManager, to:
 * Fetch reminders that are due within the next 15 minutes.
 * Send a notification if any reminder is found and is:
 * Not marked as completed.
 * Not a holiday.

 */


class ReminderWorker(
    context: Context,
    workParams: WorkerParameters
) : CoroutineWorker(context, workParams) {
    /**
     *
     * This defines a custom Worker using Kotlin coroutines.
     * CoroutineWorker allows the use of suspend functions (like database calls).
     *
     */
    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val channelId = "reminder_channel"

    override suspend fun doWork(): Result {
        createNotificationChannel()
        checkAndNotifyReminders()
        return Result.success()
    }

    /**
     * It looks at local database for reminders within the next 15 mins.
     * If there’s any that’s not done and not a holiday, it sends a notification.
     */
    private suspend fun checkAndNotifyReminders() {
        val database = ReminderDatabase.getDatabase(applicationContext)
        val currentTime = System.currentTimeMillis()

        database.reminderDao().getRemindersForDay(
            currentTime,
            currentTime + TimeUnit.MINUTES.toMillis(15)
        ).collect { reminders ->
            reminders.forEach { reminder ->
                if (!reminder.isCompleted && !reminder.isHoliday) {
                    showNotification(reminder)
                }
            }
        }
    }

    /**
     * Clicking the notification opens the app.
     */

    private fun showNotification(reminder: Reminder) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(reminder.title)
            .setContentText(reminder.description ?: "Time to focus!")
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(reminder.id.toInt(), notification)
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminder notifications"
                enableLights(true)
                lightColor = Color.BLUE
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}