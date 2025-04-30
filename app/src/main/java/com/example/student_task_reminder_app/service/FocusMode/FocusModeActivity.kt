package com.example.student_task_reminder_app.service.FocusMode

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.student_task_reminder_app.R


class FocusModeActivity : AppCompatActivity() {
    private lateinit var timerTextView: TextView
    private lateinit var stopButton: Button
    private lateinit var focusModeTitle: TextView

    private var timer: CountDownTimer? = null
    private var focusService: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_focus_mode)
        timerTextView = findViewById(R.id.timerTextView)
        stopButton = findViewById(R.id.tvFocusButtonStop)
        focusModeTitle = findViewById(R.id.tvFocusModeTitle)

        setupTitle()
        setupTimer()
        setupStopButton()
        startFocusService()


    }

    private fun setupTitle() {
        val reminderTitle = intent.getStringExtra("reminder_title")
        focusModeTitle.text = reminderTitle
    }

    private fun setupTimer() {
        val duration = 25 * 60 * 1000L // 25 minutes
        timer = object : CountDownTimer(duration, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerTextView.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                finish()
            }
        }.start()
    }

    private fun setupStopButton() {
        stopButton.setOnClickListener {
            timer?.cancel()
            stopFocusService()
            finish()
        }
    }

    private fun startFocusService() {
        focusService = Intent(this, FocusModeService::class.java)
        startForegroundService(focusService)
    }

    private fun stopFocusService() {
        focusService?.let {
            stopService(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

}