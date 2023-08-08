package com.example.drive360.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.drive360.DataClass.User
import com.google.firebase.database.*
import com.example.sagarmiles.R

class CreateProfile : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_profile)

        database = FirebaseDatabase.getInstance().getReference("users")

        val phoneNo = intent.getStringExtra("Phone")

        val buttonSubmit : Button = findViewById(R.id.buttonSubmit)
        val editTextName : EditText = findViewById(R.id.editTextName)
        val editTextAge : EditText = findViewById(R.id.editTextAge)
        val editTextLocation : EditText = findViewById(R.id.editTextLocation)

        buttonSubmit.setOnClickListener {

            val name = editTextName.text.toString()
            val age = editTextAge.text.toString().toInt()
            val location = editTextLocation.text.toString()

            val user = User(name, phoneNo!!, age, location, "none")

            // You can choose a suitable child name. Here, "users" is used.
            database.child(phoneNo).setValue(user)
                .addOnSuccessListener {
                    // Data successfully written
                    Toast.makeText(this, "Profile created successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    // Write failed
                    Toast.makeText(this, "Failed to create", Toast.LENGTH_SHORT).show()
                }
        }



    }
}

