package com.example.student_task_reminder_app

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.student_task_reminder_app.data.database.ReminderDatabase
import com.example.student_task_reminder_app.data.repository.ReminderRepository
import com.example.student_task_reminder_app.service.WorkManager.ReminderWorker
import com.example.student_task_reminder_app.ui.adapters.ReminderViewPagerAdapter
import com.example.student_task_reminder_app.ui.viewmodels.ReminderViewModel
import com.example.student_task_reminder_app.ui.viewmodels.ReminderViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigation: BottomNavigationView

    lateinit var viewModel: ReminderViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d("MyApp", "App started")

        val database = ReminderDatabase.getDatabase(applicationContext)
        val repository = ReminderRepository(database.reminderDao())

        val factory = ReminderViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ReminderViewModel::class.java]


        setupViewPager()
        setupBottomNavigation()
        setupWorkManager()
    }

    private fun setupViewPager() {
        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = ReminderViewPagerAdapter(this)
        viewPager.isUserInputEnabled = false //Disable swiping between fragments
    }

    private fun setupBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottomNavigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_today -> viewPager.currentItem = 0
                R.id.navigation_upcoming -> viewPager.currentItem = 1
                R.id.navigation_completed -> viewPager.currentItem = 2
            }
            true
        }
    }

    private fun setupWorkManager() {
        val workManager = WorkManager.getInstance(this)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val reminderWork = PeriodicWorkRequestBuilder<ReminderWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "reminderWorker",
            androidx.work.ExistingPeriodicWorkPolicy.KEEP,
            reminderWork
        )
    }
}