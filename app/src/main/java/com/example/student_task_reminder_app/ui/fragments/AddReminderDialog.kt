package com.example.student_task_reminder_app.ui.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.student_task_reminder_app.R
import com.example.student_task_reminder_app.data.entity.Reminder
import com.example.student_task_reminder_app.ui.viewmodels.ReminderViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * A dialog for adding a new reminder.
 *
 * This fragment displays a dialog that allows users to input a title, description,
 * date, and time for a reminder. It validates user input and submits the data
 * to the shared [ReminderViewModel].
 *
 * This dialog uses [MaterialAlertDialogBuilder] for styling and handles date
 * and time input using [DatePickerDialog] and [TimePickerDialog].
 */


class AddReminderDialog : DialogFragment() {
    private lateinit var viewModel: ReminderViewModel
    private var selectedDateTime: Calendar = Calendar.getInstance()

    /**
     * Called to create the dialog.
     * Inflates the layout, initializes view model, and sets up interactions.
     */

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel = ViewModelProvider(requireActivity())[ReminderViewModel::class.java]

        val view = layoutInflater.inflate(R.layout.dialog_add_reminder, null)

        val builder = MaterialAlertDialogBuilder(requireContext())
            .setView(view)
            .setTitle("Add Reminder")
        setupDateTimePickers(view)
        setupButtons(view, builder)
        return builder.create()
    }

    private fun setupDateTimePickers(view: View) {

        val dateEditText = view.findViewById<TextInputEditText>(R.id.etDate)
        val timeEditText = view.findViewById<TextInputEditText>(R.id.etTime)

        dateEditText.setOnClickListener {
            showDatePicker(view)
        }

        timeEditText.setOnClickListener {
            showTimePicker(view)
        }

    }

    private fun setupButtons(view: View, builder: MaterialAlertDialogBuilder) {
        view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dismiss()
        }

        view.findViewById<Button>(R.id.btnSave).setOnClickListener {

            val titleLayout = view.findViewById<TextInputLayout>(R.id.etTitle)
            val title = titleLayout.editText?.text.toString()

            val descriptionLayout = view.findViewById<TextInputLayout>(R.id.etDescription)
            val description =
                descriptionLayout.editText?.text.toString()


            if (title.isNotEmpty()) {
                val reminder = Reminder(
                    title = title,
                    description = description,
                    dateTime = selectedDateTime.timeInMillis
                )
                Log.d("AddReminderDialog", "Reminder to be added: $reminder")
                viewModel.insertReminder(reminder)
                Log.d("AddReminderDialog", "Reminder added: $reminder")
                dismiss()
            } else {
                titleLayout.error = "Title is required"
            }
        }
    }

    /**
     * Opens a [DatePickerDialog] and updates the selected date.
     */
    private fun showDatePicker(view: View) {
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                selectedDateTime.set(Calendar.YEAR, year)
                selectedDateTime.set(Calendar.MONTH, month)
                selectedDateTime.set(Calendar.DAY_OF_MONTH, day)
                updateDateTimeDisplay(view)
            },
            selectedDateTime.get(Calendar.YEAR),
            selectedDateTime.get(Calendar.MONTH),
            selectedDateTime.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker(view: View) {
        TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                selectedDateTime.set(Calendar.HOUR_OF_DAY, hour)
                selectedDateTime.set(Calendar.MINUTE, minute)
                updateDateTimeDisplay(view)
            },
            selectedDateTime.get(Calendar.HOUR_OF_DAY),
            selectedDateTime.get(Calendar.MINUTE),
            false
        ).show()
    }


    /**
     * Updates the text fields in the dialog with the selected date and time.
     */
    private fun updateDateTimeDisplay(view: View) {
        view.findViewById<TextInputEditText>(R.id.etDate)?.setText(
            SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                .format(selectedDateTime.time)
        )
        view.findViewById<TextInputEditText>(R.id.etTime)?.setText(
            SimpleDateFormat("hh:mm a", Locale.getDefault())
                .format(selectedDateTime.time)
        )
    }
}