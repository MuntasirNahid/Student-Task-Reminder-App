package com.example.student_task_reminder_app.data.dao

import android.database.Cursor
import androidx.room.*
import com.example.student_task_reminder_app.data.entity.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    /*
    Flow<List<Reminder>>: It’s a cold asynchronous stream. It emits data whenever there's a change in DB.
    suspend fun: One-shot, runs once inside coroutine, waits for result.
     */

    @Query("SELECT * FROM reminders WHERE isCompleted = 0 AND dateTime >= :startTime AND dateTime < :endTime ORDER BY dateTime ASC")
    fun getRemindersForDay(startTime: Long, endTime: Long): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE dateTime > :currentTime AND isCompleted = 0 ORDER BY dateTime ASC")
    fun getUpcomingReminders(currentTime: Long): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE isCompleted = 1 ORDER BY dateTime DESC")
    fun getCompletedReminders(): Flow<List<Reminder>>


    /*
    Room database operations like insert, update, delete can take time (disk I/O).
    If you run them on the main/UI thread, the app might freeze or crash.
    suspend allows these functions to be run inside a coroutine — a Kotlin feature that lets you do asynchronous work without blocking the main thread.
     */

    @Insert
    suspend fun insertReminder(reminder: Reminder): Long

    @Update
    suspend fun updateReminder(reminder: Reminder)

    @Delete
    suspend fun deleteReminder(reminder: Reminder)

    @Query("SELECT * FROM reminders")
    fun getRemindersCursor(): Cursor

    @Query("SELECT * FROM reminders WHERE id = :id")
    fun getReminderCursor(id: Long?): Cursor
}