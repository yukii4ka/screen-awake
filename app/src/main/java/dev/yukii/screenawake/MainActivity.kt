package dev.yukii.screenawake

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn15m = findViewById<Button>(R.id.btn15m)
        val btn30m = findViewById<Button>(R.id.btn30m)
        val btn1h = findViewById<Button>(R.id.btn1h)
        val btnCustom = findViewById<Button>(R.id.btnCustom)
        val picker = findViewById<NumberPicker>(R.id.picker)

        picker.minValue = 1
        picker.maxValue = 120
        picker.value = 30

        btn15m.setOnClickListener { startAwake(15 * 60 * 1000L) }
        btn30m.setOnClickListener { startAwake(30 * 60 * 1000L) }
        btn1h.setOnClickListener { startAwake(60 * 60 * 1000L) }
        btnCustom.setOnClickListener { startAwake(picker.value * 60 * 1000L) }
    }

    private fun startAwake(durationMs: Long) {
        val intent = Intent(this, AwakeService::class.java).apply {
            putExtra(AwakeService.EXTRA_DURATION_MS, durationMs)
        }
        startForegroundService(intent)
        finish()
    }
}
