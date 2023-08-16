package com.drive.drive360.Activity

import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import com.squareup.picasso.Picasso
import com.drive.drive360.R

class DriverProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_profile)

        val codeTextView : TextView = findViewById(R.id.codeTextView)
        val driverImageView: ImageView = findViewById(R.id.driverImageView)
        val nameTextView: TextView = findViewById(R.id.nameTextView)
        val ageTextView: TextView = findViewById(R.id.ageTextView)
        val phoneTextView: TextView = findViewById(R.id.phoneTextView)
        val experienceTextView: TextView = findViewById(R.id.experienceTextView)
        val salaryTextView: TextView = findViewById(R.id.salaryTextView)
        val hiringFeeTextView: TextView = findViewById(R.id.hiringFeeTextView)
        val addressTextView : TextView = findViewById(R.id.addressTextView)
        val certificateImageView: ImageButton = findViewById(R.id.certificateImageView)
        val hireButton: Button = findViewById(R.id.hireButton)

        // Get the data from the intent
        val code = intent.getIntExtra("code", 0)
        val name = intent.getStringExtra("name")
        val image = intent.getStringExtra("image")
        val age = intent.getIntExtra("age", 0)
        val phoneNo = intent.getStringExtra("phoneNo")
        val address = intent.getStringExtra("address")
        val experience = intent.getIntExtra("experience", 0)
        val salaryDemanded = intent.getStringExtra("salaryDemanded")
        val hiringFee = intent.getStringExtra("hiringFee")
        val certificate = intent.getStringExtra("certificate")

        // Set the data to the corresponding views
        Picasso.get().load(image).into(driverImageView)
        codeTextView.text = code.toString()
        nameTextView.text = name
        ageTextView.text = "Age: $age"
        phoneTextView.text = "Phone: $phoneNo"
        experienceTextView.text = "Experience: $experience years"
        salaryTextView.text = "Salary Demanded: ₹$salaryDemanded"
        hiringFeeTextView.text = "Hiring Fee: ₹$hiringFee"
        addressTextView.text = "Address: $address"


        certificateImageView.setOnClickListener {
            var manager : DownloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val pdfLink = Uri.parse(certificate)
            var request = DownloadManager.Request(pdfLink)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            manager.enqueue(request)
            // Handle certificate download logic
        }

        hireButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("HIRE NOW")
            builder.setMessage("Are you sure?")
            builder.setPositiveButton("YES", DialogInterface.OnClickListener{ dialog, which->
                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra("code",code)
                intent.putExtra("name",name)
                intent.putExtra("address",address)
                intent.putExtra("salary", salaryDemanded)
                intent.putExtra("hiringFee", hiringFee)
                startActivity(intent)
            })
            builder.setNegativeButton("NO", DialogInterface.OnClickListener{ dialog, which-> })
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
    }
}
