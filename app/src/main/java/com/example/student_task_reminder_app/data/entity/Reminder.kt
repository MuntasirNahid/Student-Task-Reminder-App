package com.example.student_task_reminder_app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String?,
    val dateTime: Long,
    val isCompleted: Boolean = false,
    val isHoliday: Boolean = false
)
