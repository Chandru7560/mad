package com.lab.reminder

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var etMessage: EditText
    private lateinit var btnSetReminder: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etMessage = findViewById(R.id.etMessage)
        btnSetReminder = findViewById(R.id.btnSetReminder)

        createNotificationChannel()

        btnSetReminder.setOnClickListener {
            val message = etMessage.text.toString()
            if (message.isNotEmpty()) {
                scheduleReminder(message)
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ReminderChannel"
            val descriptionText = "Channel for Reminder Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("reminder_id", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun scheduleReminder(message: String) {
        val intent = Intent(this, ReminderBroadcast::class.java).apply {
            putExtra("message", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val timeToWait = System.currentTimeMillis() + 5000 // 5 seconds later

        try {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeToWait, pendingIntent)
            Toast.makeText(this, "Reminder set for 5 seconds!", Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            Toast.makeText(this, "Exact alarm permission needed", Toast.LENGTH_SHORT).show()
        }
    }
}
