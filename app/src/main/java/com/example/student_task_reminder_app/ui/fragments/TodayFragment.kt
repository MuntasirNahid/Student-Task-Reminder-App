package com.example.student_task_reminder_app.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.student_task_reminder_app.R
import com.example.student_task_reminder_app.data.entity.Reminder
import com.example.student_task_reminder_app.service.FocusMode.FocusModeActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TodayFragment : BaseReminderFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FragmentCheck", "onCreate triggered in ${this::class.simpleName}")

        viewModel.loadTodayReminders()

        Log.d("TodayFragment", "TodayFragment created and viewModel loaded with today reminders")

    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_today
    }

    override fun getRecyclerViewId(): Int {
        return R.id.recyclerViewToday
    }

    override fun setupObservers() {
        //viewModel.loadTodayReminders()
        viewModel.todayReminders.observe(viewLifecycleOwner) { reminders ->

            try {
                Log.d("TodayFragment", "Submitting list with ${reminders.size} reminders")
                reminderAdapter.submitList(reminders)
            } catch (e: Exception) {
                Log.e("TodayFragment", "Error submitting list to adapter", e)
            }

        }

        requireView().findViewById<FloatingActionButton>(R.id.fabAddReminder).setOnClickListener {
            showAddReminderDialog()
        }
    }

    private fun showAddReminderDialog() {
        AddReminderDialog().show(childFragmentManager, "AddReminderDialog")
    }

    override fun startFocusMode(reminder: Reminder) {
        val intent = Intent(requireContext(), FocusModeActivity::class.java).apply {
            putExtra("reminder_title",reminder.title)
        }

        startActivity(intent)
    }


}