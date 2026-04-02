package com.lab.allinonelab

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// ============================================================
// TOPIC 3: ACTIVITY & INTENT - MainActivity (Launcher Activity)
// ============================================================
// An Activity represents a single screen with a user interface.
// Intent is used to navigate between activities or invoke other apps.

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Creating layout programmatically (can also use XML)
        val scrollView = ScrollView(this)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 40, 40, 40)
        }

        val title = TextView(this).apply {
            text = "📱 All-In-One Android Lab"
            textSize = 24f
            setPadding(0, 0, 0, 30)
        }
        layout.addView(title)

        // --- Button to open each topic ---

        // 1. Layouts Demo
        val btnLayouts = Button(this).apply {
            text = "1. Different Layouts"
            setOnClickListener {
                // EXPLICIT INTENT - navigating to LayoutsActivity
                val intent = Intent(this@MainActivity, LayoutsActivity::class.java)
                startActivity(intent)
            }
        }
        layout.addView(btnLayouts)

        // 2. Views & Widgets Demo
        val btnViews = Button(this).apply {
            text = "2. Views & Widgets"
            setOnClickListener {
                startActivity(Intent(this@MainActivity, ViewsWidgetsActivity::class.java))
            }
        }
        layout.addView(btnViews)

        // 3. Activity & Intent Demo
        val btnIntent = Button(this).apply {
            text = "3. Activity & Intent"
            setOnClickListener {
                // EXPLICIT INTENT with data passing
                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                intent.putExtra("USER_NAME", "Lab Student")
                intent.putExtra("ROLL_NO", 101)
                startActivity(intent)
            }
        }
        layout.addView(btnIntent)

        // 4. Implicit Intent - Open Browser
        val btnImplicit = Button(this).apply {
            text = "3b. Implicit Intent (Open Browser)"
            setOnClickListener {
                // IMPLICIT INTENT - system decides which app handles it
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = android.net.Uri.parse("https://www.google.com")
                startActivity(intent)
            }
        }
        layout.addView(btnImplicit)

        // 5. Menus Demo
        val btnMenu = Button(this).apply {
            text = "4. Menus"
            setOnClickListener {
                startActivity(Intent(this@MainActivity, MenuActivity::class.java))
            }
        }
        layout.addView(btnMenu)

        // 6. Dialogs & Notifications Demo
        val btnDialog = Button(this).apply {
            text = "5. Dialogs & Notifications"
            setOnClickListener {
                startActivity(Intent(this@MainActivity, DialogNotificationActivity::class.java))
            }
        }
        layout.addView(btnDialog)

        // 7. Location Based Services
        val btnLocation = Button(this).apply {
            text = "6. Location Based Services"
            setOnClickListener {
                startActivity(Intent(this@MainActivity, LocationActivity::class.java))
            }
        }
        layout.addView(btnLocation)

        // 8. Data Storage
        val btnStorage = Button(this).apply {
            text = "7. Data Storage"
            setOnClickListener {
                startActivity(Intent(this@MainActivity, DataStorageActivity::class.java))
            }
        }
        layout.addView(btnStorage)

        scrollView.addView(layout)
        setContentView(scrollView)
    }
}
