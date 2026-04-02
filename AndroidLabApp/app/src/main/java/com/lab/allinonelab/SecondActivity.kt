package com.lab.allinonelab

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// ============================================================
// TOPIC 3: ACTIVITY & INTENT (SecondActivity)
// ============================================================
// This activity receives data from MainActivity via Intent extras.
// It demonstrates:
//   - Receiving data using intent.getStringExtra() / getIntExtra()
//   - Sending result back using setResult()

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        title = "Second Activity (Intent Demo)"

        val tvReceivedData = findViewById<TextView>(R.id.tvReceivedData)
        val etReply = findViewById<EditText>(R.id.etReply)
        val btnSendBack = findViewById<Button>(R.id.btnSendBack)

        // --- Receiving data from Intent ---
        val userName = intent.getStringExtra("USER_NAME") ?: "No Name"
        val rollNo = intent.getIntExtra("ROLL_NO", 0)

        tvReceivedData.text = "Received Data:\nName: $userName\nRoll No: $rollNo"

        // --- Sending data back ---
        btnSendBack.setOnClickListener {
            val replyIntent = Intent()
            replyIntent.putExtra("REPLY_MESSAGE", etReply.text.toString())
            setResult(RESULT_OK, replyIntent)
            finish() // Close this activity and go back
        }
    }

    // Activity Lifecycle Methods (important for exams!)
    override fun onStart() {
        super.onStart()
        // Called when activity becomes visible
    }

    override fun onResume() {
        super.onResume()
        // Called when activity starts interacting with user
    }

    override fun onPause() {
        super.onPause()
        // Called when activity is partially visible
    }

    override fun onStop() {
        super.onStop()
        // Called when activity is no longer visible
    }

    override fun onDestroy() {
        super.onDestroy()
        // Called before activity is destroyed
    }

    override fun onRestart() {
        super.onRestart()
        // Called when activity is restarting after being stopped
    }
}
