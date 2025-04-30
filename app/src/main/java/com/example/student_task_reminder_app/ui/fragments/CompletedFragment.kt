package com.example.student_task_reminder_app.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.student_task_reminder_app.R
import com.example.student_task_reminder_app.data.entity.Reminder

class CompletedFragment : BaseReminderFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FragmentCheck", "onCreate triggered in ${this::class.simpleName}")

        viewModel.loadCompletedReminders()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_completed
    }

    override fun getRecyclerViewId(): Int {
        return R.id.recyclerViewCompleted
    }

    override fun setupObservers() {
        viewModel.completedReminders.observe(viewLifecycleOwner) { reminders ->
            reminderAdapter.submitList(reminders)
        }
    }

    override fun startFocusMode(reminder: Reminder) {
        TODO("Not yet implemented")
    }
}