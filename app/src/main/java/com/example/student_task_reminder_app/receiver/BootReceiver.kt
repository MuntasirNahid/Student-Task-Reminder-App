package com.example.student_task_reminder_app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.student_task_reminder_app.service.WorkManager.ReminderWorker
import java.util.concurrent.TimeUnit

/**
 * This BootReceiver is a BroadcastReceiver that listens for the device boot event (BOOT_COMPLETED).
 * When the phone restarts, this receiver ensures ReminderWorker starts again to keep showing upcoming reminder notifications.
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val workManager = WorkManager.getInstance(context)

            // In this case, no internet required (NOT_REQUIRED) — it can run offline.
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()

            /**
             * Creates a Periodic Work Request that runs every 15 minutes.
             * Uses the ReminderWorker class .
             * Applies the no-network constraint.
             */

            val reminderWork = PeriodicWorkRequestBuilder<ReminderWorker>(
                15, TimeUnit.MINUTES
            ).setConstraints(constraints)
                .build()

            /**
             * Enqueues the work as a unique periodic job:
             * "reminderWork" is the unique name.
             * KEEP means if a job with the same name already exists, don’t replace it.
             * Schedules the reminderWork task to start every 15 minutes again after reboot.
             */


            workManager.enqueueUniquePeriodicWork(
                "reminderWorker",
                androidx.work.ExistingPeriodicWorkPolicy.KEEP,
                reminderWork
            )

        }
    }
}