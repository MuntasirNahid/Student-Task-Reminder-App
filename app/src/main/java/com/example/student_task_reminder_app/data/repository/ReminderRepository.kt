package com.example.student_task_reminder_app.data.repository

import android.util.Log
import com.example.student_task_reminder_app.data.dao.ReminderDao
import com.example.student_task_reminder_app.data.entity.Reminder

class ReminderRepository(private val reminderDao: ReminderDao) {
    fun getRemindersForDay(startTime: Long, endTime: Long) =
        reminderDao.getRemindersForDay(startTime, endTime)

    fun getUpcomingReminders(currentTime: Long) =
        reminderDao.getUpcomingReminders(currentTime)

    fun getCompletedReminders() =
        reminderDao.getCompletedReminders()

    suspend fun insertReminder(reminder: Reminder) =
        reminderDao.insertReminder(reminder)

    suspend fun updateReminder(reminder: Reminder) =
        reminderDao.updateReminder(reminder)

    suspend fun deleteReminder(reminder: Reminder) =
        reminderDao.deleteReminder(reminder)
}