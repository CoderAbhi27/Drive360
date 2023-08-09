package com.example.drive360.Activity

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sagarmiles.R

class FuelTrackingOrderActivity : AppCompatActivity() {
    private lateinit var ftCompany: AutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fuel_tracking_order)
        ftCompany = findViewById(R.id.ftCompany)
        val companies = arrayOf(
            "Tata Motors",
            "Eicher Motors",
            "SML ISUZU Limited",
            "Mahindra",
            "Ashok Leyland",
            "Force Motors",
            "Hindustan Motors",
            "BharatBenz",
            "Volvo Trucks",
            "Asia Motorworks"
        )

        val arrayAdapter=ArrayAdapter<String>(this, androidx.appcompat.R.layout.select_dialog_item_material,companies)
        ftCompany.threshold=0
        ftCompany.setAdapter(arrayAdapter)
    }
}