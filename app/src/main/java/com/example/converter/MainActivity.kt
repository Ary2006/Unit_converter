package com.example.converter

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.converter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val temperatureUnits = arrayOf(" Celsius", "Fahrenheit", "Kelvin")
    private val distanceUnits = arrayOf("Kilometers", "Meters", "Feet")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val unitTypes = arrayOf("Temperature", "Distance")
        binding.unitTypeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, unitTypes)

        // Change units when category changes
        binding.unitTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val units = if (position == 0) temperatureUnits else distanceUnits
                val adapter =
                    ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, units)
                binding.fromSpinner.adapter = adapter
                binding.toSpinner.adapter = adapter
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Convert on button click
        binding.convertBtn.setOnClickListener {
            val input = binding.inputValue.text.toString().toDoubleOrNull()
            if (input == null) {
                binding.resultText.text = "Enter a valid number"
                return@setOnClickListener
            }

            val fromUnit = binding.fromSpinner.selectedItem.toString()
            val toUnit = binding.toSpinner.selectedItem.toString()
            val type = binding.unitTypeSpinner.selectedItem.toString()

            val result = if (type == "Temperature") {
                convertTemperature(input, fromUnit, toUnit)
            } else {
                convertDistance(input, fromUnit, toUnit)
            }

            binding.resultText.text = "$input $fromUnit → $result $toUnit"
        }
    }

    private fun convertTemperature(value: Double, from: String, to: String): Double {
        val celsius = when (from) {
            "Celsius" -> value
            "Fahrenheit" -> (value - 32) * 5 / 9
            "Kelvin" -> value - 273.15
            else -> value
        }

        return when (to) {
            "Celsius" -> celsius
            "Fahrenheit" -> celsius * 9 / 5 + 32
            "Kelvin" -> celsius + 273.15
            else -> celsius
        }
    }

    private fun convertDistance(value: Double, from: String, to: String): Double {
        val meters = when (from) {
            "Kilometers" -> value * 1000
            "Meters" -> value
            "Feet" -> value * 0.3048
            else -> value
        }

        return when (to) {
            "Kilometers" -> meters / 1000
            "Meters" -> meters
            "Feet" -> meters / 0.3048
            else -> meters
        }
    }
}
