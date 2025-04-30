package com.example.student_task_reminder_app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.student_task_reminder_app.MainActivity
import com.example.student_task_reminder_app.data.entity.Reminder
import com.example.student_task_reminder_app.ui.adapters.ReminderAction
import com.example.student_task_reminder_app.ui.adapters.ReminderAdapter
import com.example.student_task_reminder_app.ui.viewmodels.ReminderViewModel


// An abstract base fragment that other fragments (like Today, Upcoming, Completed) will extend.
abstract class BaseReminderFragment : Fragment() {
    protected lateinit var reminderAdapter: ReminderAdapter
    protected lateinit var viewModel: ReminderViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupViewModel()
        setupObservers()
    }

    abstract fun getLayoutId(): Int
    abstract fun setupObservers()


    private fun setupRecyclerView() {
        reminderAdapter = ReminderAdapter { reminder, action ->
            when (action) {
                ReminderAction.COMPLETE -> {
                    viewModel.updateReminder(reminder.copy(isCompleted = true))
                    //Show a toast or snackbar to inform the user
                    Toast.makeText(
                        requireContext(),
                        "Reminder ${reminder.title} marked as completed",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                ReminderAction.START_FOCUS -> {
                    startFocusMode(reminder)
                }
            }
        }

        /**
         * Sets up the RecyclerView:
         * Uses the ID returned by getRecyclerViewId() (implemented in child).
         * Adds vertical layout.
         * Sets the adapter.
         * Adds divider lines between items.
         */
        requireView().findViewById<RecyclerView>(getRecyclerViewId()).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reminderAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    abstract fun getRecyclerViewId(): Int

    abstract fun startFocusMode(reminder: Reminder)

    /**
     * Sets up the shared ReminderViewModel by accessing it from MainActivity.
     * Using requireActivity() ensures the activity is not null.
     * This is a common pattern in Android development to share data between fragments and their host activity.

     */
    private fun setupViewModel() {
        viewModel = (requireActivity() as MainActivity).viewModel
    }

}