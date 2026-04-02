package com.lab.allinonelab

import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// ============================================================
// TOPIC 4: MENUS
// ============================================================
// Three types of menus in Android:
//   1. Options Menu - appears in the app bar (3-dot menu)
//   2. Context Menu - appears on long-press
//   3. Popup Menu - appears anchored to a view

class MenuActivity : AppCompatActivity() {

    private lateinit var tvMenuResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        title = "Menus Demo"

        tvMenuResult = findViewById(R.id.tvMenuResult)

        // --- Context Menu: Register a view for context menu ---
        val tvContextTarget = findViewById<TextView>(R.id.tvContextTarget)
        registerForContextMenu(tvContextTarget) // Long-press will show context menu

        // --- Popup Menu ---
        val btnPopupMenu = findViewById<Button>(R.id.btnPopupMenu)
        btnPopupMenu.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }

    // ========== 1. OPTIONS MENU ==========
    // Called once to create the options menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    // Called when an options menu item is selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_home -> {
                tvMenuResult.text = "Options Menu: Home clicked"
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
            }
            R.id.menu_settings -> {
                tvMenuResult.text = "Options Menu: Settings clicked"
            }
            R.id.menu_about -> {
                tvMenuResult.text = "Options Menu: About clicked"
            }
            R.id.menu_exit -> {
                finish() // Close the activity
            }
            R.id.menu_share_whatsapp -> {
                tvMenuResult.text = "Sub Menu: WhatsApp Share clicked"
            }
            R.id.menu_share_email -> {
                tvMenuResult.text = "Sub Menu: Email Share clicked"
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // ========== 2. CONTEXT MENU ==========
    // Called when creating context menu (on long-press)
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
        menu?.setHeaderTitle("Choose Action")
    }

    // Called when a context menu item is selected
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ctx_edit -> tvMenuResult.text = "Context Menu: Edit selected"
            R.id.ctx_delete -> tvMenuResult.text = "Context Menu: Delete selected"
            R.id.ctx_copy -> tvMenuResult.text = "Context Menu: Copy selected"
        }
        return super.onContextItemSelected(item)
    }

    // ========== 3. POPUP MENU ==========
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menu.add(0, 1, 0, "Option A")
        popupMenu.menu.add(0, 2, 1, "Option B")
        popupMenu.menu.add(0, 3, 2, "Option C")

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                1 -> tvMenuResult.text = "Popup Menu: Option A"
                2 -> tvMenuResult.text = "Popup Menu: Option B"
                3 -> tvMenuResult.text = "Popup Menu: Option C"
            }
            true
        }
        popupMenu.show()
    }
}
