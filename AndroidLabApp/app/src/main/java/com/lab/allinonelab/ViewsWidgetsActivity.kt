package com.lab.allinonelab

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

// ============================================================
// TOPIC 2: DIFFERENT VIEWS AND WIDGETS
// ============================================================
// Views are the basic building blocks of UI (TextView, Button, etc.)
// Widgets are interactive UI components (CheckBox, Spinner, etc.)

class ViewsWidgetsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_views_widgets)
        title = "Views & Widgets Demo"

        // --- Get references to all views ---
        val tvDisplay = findViewById<TextView>(R.id.tvDisplay)
        val etName = findViewById<EditText>(R.id.etName)
        val btnClick = findViewById<Button>(R.id.btnClick)
        val cbJava = findViewById<CheckBox>(R.id.cbJava)
        val cbKotlin = findViewById<CheckBox>(R.id.cbKotlin)
        val cbPython = findViewById<CheckBox>(R.id.cbPython)
        val rgGender = findViewById<RadioGroup>(R.id.rgGender)
        val switchNotify = findViewById<Switch>(R.id.switchNotify)
        val toggleBtn = findViewById<ToggleButton>(R.id.toggleBtn)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val tvSeekValue = findViewById<TextView>(R.id.tvSeekValue)
        val spinnerCity = findViewById<Spinner>(R.id.spinnerCity)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val autoComplete = findViewById<AutoCompleteTextView>(R.id.autoComplete)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        // --- 1. Button Click Listener ---
        btnClick.setOnClickListener {
            val name = etName.text.toString()
            if (name.isNotEmpty()) {
                tvDisplay.text = "Hello, $name!"
                Toast.makeText(this, "Button Clicked!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            }
        }

        // --- 2. CheckBox Listener ---
        val checkBoxListener = { _: android.widget.CompoundButton, _: Boolean ->
            val selected = mutableListOf<String>()
            if (cbJava.isChecked) selected.add("Java")
            if (cbKotlin.isChecked) selected.add("Kotlin")
            if (cbPython.isChecked) selected.add("Python")
            tvResult.text = "Selected: ${selected.joinToString(", ")}"
        }
        cbJava.setOnCheckedChangeListener(checkBoxListener)
        cbKotlin.setOnCheckedChangeListener(checkBoxListener)
        cbPython.setOnCheckedChangeListener(checkBoxListener)

        // --- 3. RadioGroup Listener ---
        rgGender.setOnCheckedChangeListener { _, checkedId ->
            val gender = when (checkedId) {
                R.id.rbMale -> "Male"
                R.id.rbFemale -> "Female"
                else -> "Unknown"
            }
            tvResult.text = "Gender: $gender"
        }

        // --- 4. Switch Listener ---
        switchNotify.setOnCheckedChangeListener { _, isChecked ->
            tvResult.text = "Notifications: ${if (isChecked) "Enabled" else "Disabled"}"
        }

        // --- 5. ToggleButton Listener ---
        toggleBtn.setOnCheckedChangeListener { _, isChecked ->
            tvResult.text = "Toggle: ${if (isChecked) "ON" else "OFF"}"
        }

        // --- 6. SeekBar Listener ---
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvSeekValue.text = "Value: $progress"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                tvResult.text = "SeekBar stopped at: ${seekBar?.progress}"
            }
        })

        // --- 7. Spinner Setup ---
        val cities = arrayOf("Chennai", "Mumbai", "Delhi", "Bangalore", "Hyderabad")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cities)
        spinnerCity.adapter = adapter
        spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, pos: Int, id: Long) {
                tvResult.text = "City: ${cities[pos]}"
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // --- 8. RatingBar Listener ---
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            tvResult.text = "Rating: $rating / 5.0"
        }

        // --- 9. AutoCompleteTextView Setup ---
        val languages = arrayOf("Java", "Kotlin", "Python", "JavaScript", "C++", "C#", "Swift", "Go", "Rust")
        val autoAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, languages)
        autoComplete.setAdapter(autoAdapter)
        autoComplete.threshold = 1 // Start suggesting after 1 character
    }
}
