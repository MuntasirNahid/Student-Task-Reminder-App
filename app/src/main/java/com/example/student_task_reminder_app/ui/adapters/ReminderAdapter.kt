package com.example.student_task_reminder_app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.student_task_reminder_app.R
import com.example.student_task_reminder_app.data.entity.Reminder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Inherits from ListAdapter, which is like RecyclerView.Adapter but smarter—it uses DiffUtil internally for efficient updates.
 * Reminder = data type
 * ReminderViewHolder = ViewHolder class
 * ReminderDiffCallback() = tells it how to compare items
 * onReminderAction = a lambda function to handle button clicks (you can pass custom actions from outside).
 */

class ReminderAdapter(
    private val onReminderAction: (Reminder, ReminderAction) -> Unit
) : ListAdapter<Reminder, ReminderAdapter.ReminderViewHolder>(ReminderDiffCallback()) {

    //Creates the layout for each row (from item_reminder.xml) and wraps it in a ViewHolder.
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reminder, parent, false)
        return ReminderViewHolder(view)
    }

    //Binds the current data item (Reminder) to the ViewHolder’s bind() function.
    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    //ViewHolder class that holds the views for each item in the RecyclerView.
    inner class ReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //Finds and stores references to the views inside each item row so we can manipulate them.
        private val titleTextView: TextView = itemView.findViewById(R.id.tvTitle)
        private val dateTImeTextView: TextView = itemView.findViewById(R.id.tvDateTime)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescription)
        private val completeButton: ImageButton = itemView.findViewById(R.id.btnComplete)
        private val startFocusButton: ImageButton = itemView.findViewById(R.id.btnFocus)

        /**
         * This is the heart of the adapter: binds the reminder data into the UI.
         * Sets text on views.
         * Handles button clicks using the onReminderAction lambda passed earlier
         */
        fun bind(reminder: Reminder) {
            titleTextView.text = reminder.title
            descriptionTextView.text = reminder.description
            dateTImeTextView.text = formatDateTime(reminder.dateTime)

            if (reminder.isCompleted) {
                completeButton.visibility = View.GONE
                startFocusButton.visibility = View.GONE
            } else {

                completeButton.visibility = View.VISIBLE
                startFocusButton.visibility = View.VISIBLE

                completeButton.setOnClickListener {
                    onReminderAction(reminder, ReminderAction.COMPLETE)
                }

                startFocusButton.setOnClickListener {
                    onReminderAction(reminder, ReminderAction.START_FOCUS)
                }
            }


        }

        //Converts the Long timestamp to a readable format like Apr 24, 2025 10:00 AM.
        private fun formatDateTime(dateTime: Long): String {
            val date = Date(dateTime)
            return SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault()).format(date)
        }
    }
}

/**
 *  This tells ListAdapter how to efficiently update the list:
 * areItemsTheSame: compares IDs
 * areContentsTheSame: compares entire object
 */
class ReminderDiffCallback : DiffUtil.ItemCallback<Reminder>() {
    override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
        return oldItem == newItem
    }
}

// A custom enum to distinguish between actions like "complete" and "start focus" when buttons are clicked.
enum class ReminderAction {
    COMPLETE,
    START_FOCUS
}