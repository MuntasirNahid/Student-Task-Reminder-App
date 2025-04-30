package com.example.student_task_reminder_app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.student_task_reminder_app.data.entity.Reminder
import com.example.student_task_reminder_app.data.repository.ReminderRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.concurrent.TimeUnit

class ReminderViewModel(private val repository: ReminderRepository) : ViewModel() {

    //	MutableLiveData|private|Mutable| Only the ViewModel can update the value.
    private val _todayReminders = MutableLiveData<List<Reminder>>()

    //	LiveData|public|Immutable|Exposed to other classes(Ui like activity or fragment) can observe the value but cannot change it.
    val todayReminders: LiveData<List<Reminder>> = _todayReminders

    private val _upcomingReminders = MutableLiveData<List<Reminder>>()
    val upcomingReminders: LiveData<List<Reminder>> = _upcomingReminders

    private val _completedReminders = MutableLiveData<List<Reminder>>()
    val completedReminders: LiveData<List<Reminder>> = _completedReminders

    fun loadTodayReminders() {
        viewModelScope.launch {
            val startOfDay = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }.timeInMillis

            val endOfDay = startOfDay + TimeUnit.DAYS.toMillis(1)

            Log.d("ReminderViewModel", "Fetching reminders from $startOfDay to $endOfDay")

//
//            repository.getRemindersForDay(startOfDay, endOfDay)
//                .collect { reminders ->
//                    _todayReminders.value = reminders
//                    Log.d("ReminderViewModel", "Fetched reminders: $reminders")
//                }

            try {
                repository.getRemindersForDay(startOfDay, endOfDay)
                    .collect { reminders ->
                        Log.d("ReminderViewModel", "Collected ${reminders.size} reminders")
                        _todayReminders.value = reminders
                    }
            } catch (e: Exception) {
                Log.e("ReminderViewModel", "Error fetching reminders", e)
            }

        }
    }

    fun loadUpcomingReminders() {
        viewModelScope.launch {
            //val currentTime = System.currentTimeMillis()
            val now = LocalDate.now()
            val tomorrow = now.plusDays(1)
            val tomorrowTime =
                tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

            repository.getUpcomingReminders(tomorrowTime)
                .collect { reminders ->
                    _upcomingReminders.value = reminders
                }
        }
    }

    fun loadCompletedReminders() {
        viewModelScope.launch {
            repository.getCompletedReminders()
                .collect { reminders ->
                    _completedReminders.value = reminders
                }
        }
    }

    fun updateReminder(reminder: Reminder) {
        viewModelScope.launch {
            repository.updateReminder(reminder)
        }
    }

    fun insertReminder(reminder: Reminder) {
        viewModelScope.launch {
            Log.d("ReminderViewModel", "Inserting reminder: $reminder")
            repository.insertReminder(reminder)
            Log.d("ReminderViewModel", "Inserted reminder: $reminder")
        }
    }
}