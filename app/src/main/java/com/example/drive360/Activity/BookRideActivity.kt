package com.example.drive360.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.sagarmiles.R

class BookRideActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_ride)

        val submitButton : Button = findViewById(R.id.submitButton)

        submitButton.setOnClickListener{
            val intent = Intent(this, BookRidePaymentActivity::class.java)
            startActivity(intent)
        }
    }
}