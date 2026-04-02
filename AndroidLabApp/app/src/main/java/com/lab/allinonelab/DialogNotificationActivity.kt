package com.lab.allinonelab

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import java.util.Calendar

// ============================================================
// TOPIC 5: DIALOGS AND NOTIFICATIONS
// ============================================================
// Dialogs: Popup windows for user interaction
//   - AlertDialog (with buttons)
//   - List Dialog (with list items)
//   - DatePickerDialog
//   - TimePickerDialog
//   - Custom Dialog
// Notifications: Messages shown in the notification bar

class DialogNotificationActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private val CHANNEL_ID = "lab_channel"
    private val NOTIFICATION_ID = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_notification)
        title = "Dialogs & Notifications"

        tvResult = findViewById(R.id.tvDialogResult)

        // Create notification channel (required for Android 8.0+)
        createNotificationChannel()

        // --- 1. Alert Dialog ---
        findViewById<Button>(R.id.btnAlertDialog).setOnClickListener {
            showAlertDialog()
        }

        // --- 2. List Dialog ---
        findViewById<Button>(R.id.btnListDialog).setOnClickListener {
            showListDialog()
        }

        // --- 3. Date Picker ---
        findViewById<Button>(R.id.btnDatePicker).setOnClickListener {
            showDatePicker()
        }

        // --- 4. Time Picker ---
        findViewById<Button>(R.id.btnTimePicker).setOnClickListener {
            showTimePicker()
        }

        // --- 5. Custom Input Dialog ---
        findViewById<Button>(R.id.btnProgressDialog).setOnClickListener {
            showCustomInputDialog()
        }

        // --- 6. Notification ---
        findViewById<Button>(R.id.btnNotification).setOnClickListener {
            showNotification()
        }
    }

    // ========== 1. ALERT DIALOG ==========
    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Action")
        builder.setMessage("Do you want to proceed?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        // Positive button
        builder.setPositiveButton("Yes") { dialog, _ ->
            tvResult.text = "Alert Dialog: You clicked YES"
            dialog.dismiss()
        }

        // Negative button
        builder.setNegativeButton("No") { dialog, _ ->
            tvResult.text = "Alert Dialog: You clicked NO"
            dialog.dismiss()
        }

        // Neutral button
        builder.setNeutralButton("Cancel") { dialog, _ ->
            tvResult.text = "Alert Dialog: Cancelled"
            dialog.dismiss()
        }

        builder.setCancelable(false) // User must click a button
        builder.show()
    }

    // ========== 2. LIST DIALOG ==========
    private fun showListDialog() {
        val items = arrayOf("Android", "iOS", "Windows", "Linux")

        AlertDialog.Builder(this)
            .setTitle("Choose an OS")
            .setItems(items) { _, which ->
                tvResult.text = "List Dialog: Selected ${items[which]}"
            }
            .show()
    }

    // ========== 3. DATE PICKER DIALOG ==========
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                tvResult.text = "Date: $selectedDay/${selectedMonth + 1}/$selectedYear"
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    // ========== 4. TIME PICKER DIALOG ==========
    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                tvResult.text = "Time: $selectedHour:${String.format("%02d", selectedMinute)}"
            },
            hour, minute, true // true = 24-hour format
        )
        timePickerDialog.show()
    }

    // ========== 5. CUSTOM INPUT DIALOG ==========
    private fun showCustomInputDialog() {
        val editText = EditText(this).apply {
            hint = "Enter your feedback"
            setPadding(50, 30, 50, 30)
        }

        AlertDialog.Builder(this)
            .setTitle("Feedback")
            .setMessage("Please enter your feedback:")
            .setView(editText) // Custom view inside dialog
            .setPositiveButton("Submit") { _, _ ->
                val feedback = editText.text.toString()
                tvResult.text = "Feedback: $feedback"
                Toast.makeText(this, "Feedback submitted!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // ========== 6. NOTIFICATION ==========
    private fun createNotificationChannel() {
        // Notification channels are required for Android 8.0 (API 26) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Lab Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for lab demo notifications"
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun showNotification() {
        // Intent to open when notification is tapped
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Lab Notification")
            .setContentText("This is a sample notification from the lab app!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent) // Action on tap
            .setAutoCancel(true) // Dismiss on tap
            .build()

        // Show the notification
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)

        tvResult.text = "Notification sent! Check the notification bar."
        Toast.makeText(this, "Notification sent!", Toast.LENGTH_SHORT).show()
    }
}
