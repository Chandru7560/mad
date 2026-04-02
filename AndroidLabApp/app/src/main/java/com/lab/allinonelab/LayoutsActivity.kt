package com.lab.allinonelab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

// ============================================================
// TOPIC 1: DIFFERENT LAYOUTS
// ============================================================
// Layout Types demonstrated in activity_layouts.xml:
//   1. LinearLayout (Vertical) - arranges views in a single column
//   2. LinearLayout (Horizontal) - arranges views in a single row
//   3. RelativeLayout - positions views relative to parent/siblings
//   4. FrameLayout - stacks views on top of each other
//   5. TableLayout - arranges views in rows and columns
//   6. ConstraintLayout - flexible positioning with constraints

class LayoutsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layouts)
        title = "Different Layouts Demo"
    }
}
