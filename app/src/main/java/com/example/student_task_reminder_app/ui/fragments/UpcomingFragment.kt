package com.example.student_task_reminder_app.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.student_task_reminder_app.R
import com.example.student_task_reminder_app.data.entity.Reminder
import com.example.student_task_reminder_app.service.FocusMode.FocusModeActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UpcomingFragment : BaseReminderFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FragmentCheck", "onCreate triggered in ${this::class.simpleName}")

        viewModel.loadUpcomingReminders()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_upcoming
    }

    override fun getRecyclerViewId(): Int {
        return R.id.recyclerViewUpcoming
    }

    override fun setupObservers() {

        viewModel.upcomingReminders.observe(viewLifecycleOwner) { reminders ->
            reminderAdapter.submitList(reminders)
        }
        view?.findViewById<FloatingActionButton>(R.id.fabAddReminder)?.setOnClickListener {
            showAddReminderDialog()
        }
    }

    private fun showAddReminderDialog() {
        AddReminderDialog().show(childFragmentManager, "AddReminderDialog")
    }

    override fun startFocusMode(reminder: Reminder) {
        val intent = Intent(requireContext(), FocusModeActivity::class.java).apply {
            putExtra("reminder_title", reminder.title)
        }
        startActivity(intent)
    }
}