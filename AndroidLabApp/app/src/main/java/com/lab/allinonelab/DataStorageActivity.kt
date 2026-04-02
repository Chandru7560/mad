package com.lab.allinonelab

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.FileInputStream
import java.io.FileOutputStream

// ============================================================
// TOPIC 7: DATA STORAGE
// ============================================================
// Android provides multiple ways to store data:
//   1. SharedPreferences - Key-value pairs (simple data)
//   2. SQLite Database - Structured relational data
//   3. Internal Storage - Files stored in app's private directory
//   4. External Storage - Files stored in shared storage
//   5. Content Providers - Share data between apps

class DataStorageActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private lateinit var dbHelper: StudentDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_storage)
        title = "Data Storage Demo"

        tvResult = findViewById(R.id.tvStorageResult)
        dbHelper = StudentDatabaseHelper(this)

        setupSharedPreferences()
        setupSQLite()
        setupInternalStorage()
    }

    // ===================================================================
    // 1. SHARED PREFERENCES
    // ===================================================================
    // Stores key-value pairs in XML file
    // Best for: settings, user preferences, small data
    private fun setupSharedPreferences() {
        val etKey = findViewById<EditText>(R.id.etPrefKey)
        val etValue = findViewById<EditText>(R.id.etPrefValue)

        // --- Save to SharedPreferences ---
        findViewById<Button>(R.id.btnPrefSave).setOnClickListener {
            val key = etKey.text.toString()
            val value = etValue.text.toString()

            if (key.isNotEmpty() && value.isNotEmpty()) {
                // Get SharedPreferences object
                val sharedPref = getSharedPreferences("MyLabPrefs", Context.MODE_PRIVATE)
                
                // Use editor to save data
                val editor = sharedPref.edit()
                editor.putString(key, value)
                editor.apply() // apply() is async, commit() is sync

                tvResult.text = "SharedPref SAVED: $key = $value"
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
            }
        }

        // --- Load from SharedPreferences ---
        findViewById<Button>(R.id.btnPrefLoad).setOnClickListener {
            val key = etKey.text.toString()

            if (key.isNotEmpty()) {
                val sharedPref = getSharedPreferences("MyLabPrefs", Context.MODE_PRIVATE)
                val value = sharedPref.getString(key, "NOT FOUND") // default value
                tvResult.text = "SharedPref LOADED: $key = $value"
            }
        }
    }

    // ===================================================================
    // 2. SQLITE DATABASE
    // ===================================================================
    // Stores structured data in a local relational database
    // Best for: complex data, queries, relationships
    private fun setupSQLite() {
        val etName = findViewById<EditText>(R.id.etStudentName)
        val etMarks = findViewById<EditText>(R.id.etStudentMarks)

        // --- Insert into SQLite ---
        findViewById<Button>(R.id.btnDbInsert).setOnClickListener {
            val name = etName.text.toString()
            val marks = etMarks.text.toString()

            if (name.isNotEmpty() && marks.isNotEmpty()) {
                val result = dbHelper.insertStudent(name, marks.toInt())
                if (result != -1L) {
                    tvResult.text = "SQLite: Inserted '$name' with marks $marks"
                    Toast.makeText(this, "Inserted!", Toast.LENGTH_SHORT).show()
                } else {
                    tvResult.text = "SQLite: Insert FAILED"
                }
            }
        }

        // --- View all from SQLite ---
        findViewById<Button>(R.id.btnDbView).setOnClickListener {
            val students = dbHelper.getAllStudents()
            if (students.isNotEmpty()) {
                tvResult.text = "SQLite Records:\n$students"
            } else {
                tvResult.text = "SQLite: No records found"
            }
        }

        // --- Delete all from SQLite ---
        findViewById<Button>(R.id.btnDbDelete).setOnClickListener {
            dbHelper.deleteAll()
            tvResult.text = "SQLite: All records deleted"
            Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show()
        }
    }

    // ===================================================================
    // 3. INTERNAL FILE STORAGE
    // ===================================================================
    // Stores files in app's private internal storage
    // Files are deleted when app is uninstalled
    private fun setupInternalStorage() {
        val etContent = findViewById<EditText>(R.id.etFileContent)
        val fileName = "lab_data.txt"

        // --- Save to Internal File ---
        findViewById<Button>(R.id.btnFileSave).setOnClickListener {
            val content = etContent.text.toString()
            if (content.isNotEmpty()) {
                try {
                    // Open file output stream
                    val fos: FileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
                    fos.write(content.toByteArray())
                    fos.close()

                    tvResult.text = "File SAVED: $fileName\nContent: $content"
                    Toast.makeText(this, "File saved!", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    tvResult.text = "File Save Error: ${e.message}"
                }
            }
        }

        // --- Load from Internal File ---
        findViewById<Button>(R.id.btnFileLoad).setOnClickListener {
            try {
                // Open file input stream
                val fis: FileInputStream = openFileInput(fileName)
                val content = fis.bufferedReader().readText()
                fis.close()

                tvResult.text = "File LOADED from $fileName:\n$content"
            } catch (e: Exception) {
                tvResult.text = "File Load Error: ${e.message}"
            }
        }
    }
}

// ===================================================================
// SQLite Database Helper Class
// ===================================================================
// Extends SQLiteOpenHelper to manage database creation and versioning

class StudentDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "StudentDB"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "students"
        const val COL_ID = "id"
        const val COL_NAME = "name"
        const val COL_MARKS = "marks"
    }

    // Called when database is created for the first time
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NAME TEXT NOT NULL,
                $COL_MARKS INTEGER NOT NULL
            )
        """.trimIndent()
        db?.execSQL(createTable)
    }

    // Called when database version is upgraded
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // INSERT a student
    fun insertStudent(name: String, marks: Int): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NAME, name)
            put(COL_MARKS, marks)
        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result
    }

    // READ all students
    fun getAllStudents(): String {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val sb = StringBuilder()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME))
                val marks = cursor.getInt(cursor.getColumnIndexOrThrow(COL_MARKS))
                sb.append("ID: $id | Name: $name | Marks: $marks\n")
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return sb.toString()
    }

    // UPDATE a student
    fun updateStudent(id: Int, name: String, marks: Int): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NAME, name)
            put(COL_MARKS, marks)
        }
        val result = db.update(TABLE_NAME, values, "$COL_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    // DELETE all students
    fun deleteAll() {
        val db = writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }

    // DELETE a specific student by ID
    fun deleteStudent(id: Int): Int {
        val db = writableDatabase
        val result = db.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }
}
