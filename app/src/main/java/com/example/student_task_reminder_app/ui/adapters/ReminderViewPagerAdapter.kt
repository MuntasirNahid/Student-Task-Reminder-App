package com.example.student_task_reminder_app.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.student_task_reminder_app.ui.fragments.CompletedFragment
import com.example.student_task_reminder_app.ui.fragments.TodayFragment
import com.example.student_task_reminder_app.ui.fragments.UpcomingFragment

class ReminderViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TodayFragment()
            1 -> UpcomingFragment()
            2 -> CompletedFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")

        }
    }
}