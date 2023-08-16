package com.drive.drive360.Activity

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.drive.drive360.R
import com.squareup.picasso.Picasso

class HiredDriverProfileActivity : AppCompatActivity() {

    private val GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user"
    private val GOOGLE_PAY_REQUEST_CODE = 123
    private val UPI_ID = "8102823536@ibl"
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hired_driver_profile)

        val codeTextView : TextView = findViewById(R.id.codeTextView)
        val driverImageView: ImageView = findViewById(R.id.driverImageView)
        val nameTextView: TextView = findViewById(R.id.nameTextView)
        val ageTextView: TextView = findViewById(R.id.ageTextView)
        val phoneTextView: TextView = findViewById(R.id.phoneTextView)
        val experienceTextView: TextView = findViewById(R.id.experienceTextView)
        val salaryTextView: TextView = findViewById(R.id.salaryTextView)
        val addressTextView : TextView = findViewById(R.id.addressTextView)
        val certificateImageView: ImageButton = findViewById(R.id.certificateImageView)
        val paySalaryButton: Button = findViewById(R.id.paySalaryButton)

        val code = intent.getIntExtra("code", 0)
        val name = intent.getStringExtra("name")
        val image = intent.getStringExtra("image")
        val age = intent.getIntExtra("age", 0)
        val phoneNo = intent.getStringExtra("phoneNo")
        val address = intent.getStringExtra("address")
        val experience = intent.getIntExtra("experience", 0)
        val salaryDemanded = intent.getStringExtra("salaryDemanded")
        val certificate = intent.getStringExtra("certificate")

        Picasso.get().load(image).into(driverImageView)
        codeTextView.text = code.toString()
        nameTextView.text = name
        ageTextView.text = "Age: $age"
        phoneTextView.text = "Phone: $phoneNo"
        experienceTextView.text = "Experience: $experience years"
        salaryTextView.text = "Salary Demanded: ₹$salaryDemanded"
        addressTextView.text = "Address: $address"


        certificateImageView.setOnClickListener {
            var manager : DownloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val pdfLink = Uri.parse(certificate)
            var request = DownloadManager.Request(pdfLink)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            manager.enqueue(request)
        }

        paySalaryButton.setOnClickListener{
            uri = getUpiPaymentUri(name!!, UPI_ID, "Monthly Salary", salaryDemanded!!)
            payWithGPay()
        }

    }

    private fun isAppInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getApplicationInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            true
        }
    }

    private fun getUpiPaymentUri(name: String, upiId: String, transactionNote: String, amount: String): Uri {
        return Uri.Builder()
            .scheme("upi")
            .authority("pay")
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", name)
            .appendQueryParameter("tn", transactionNote)
            .appendQueryParameter("am", amount)
            .appendQueryParameter("cu", "INR")
            .build()
    }

    private fun payWithGPay() {
        if (isAppInstalled(this, GOOGLE_PAY_PACKAGE_NAME)) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            intent.`package` = GOOGLE_PAY_PACKAGE_NAME
            startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE)
        } else {
            Toast.makeText(this@HiredDriverProfileActivity, "Please Install GPay", Toast.LENGTH_SHORT).show()
        }
    }
}