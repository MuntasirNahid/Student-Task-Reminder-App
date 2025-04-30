package com.example.student_task_reminder_app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.student_task_reminder_app.data.dao.ReminderDao
import com.example.student_task_reminder_app.data.entity.Reminder

@Database(entities = [Reminder::class], version = 1)
abstract class ReminderDatabase : RoomDatabase() {

    /*
    This function must return an instance of the ReminderDao interface.
    RoomDatabase (from Room library) automatically generates an implementation of this abstract function at compile time.
    When you call reminderDao(), Room gives you a working implementation of ReminderDao (which knows how to execute your SQL queries).
     */
    abstract fun reminderDao(): ReminderDao

    /*
    A companion object holds static members
    You want only one DB instance app-wide → singleton pattern
     */
    companion object {

        /*
        INSTANCE holds the cached singleton DB instance
        @Volatile makes sure all threads see the latest value
         */
        @Volatile
        private var INSTANCE: ReminderDatabase? = null

        fun getDatabase(context: Context): ReminderDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReminderDatabase::class.java,
                    "reminder_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}