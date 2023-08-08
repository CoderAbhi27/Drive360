package com.example.drive360.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.example.sagarmiles.R

class PaymentActivity : AppCompatActivity() {
    private lateinit var driverCodeTextView: TextView
    private lateinit var driverNameTextView: TextView
    private lateinit var driverAddressTextView: TextView
    private lateinit var driverSalaryTextView: TextView
    private lateinit var hiringFeeTextView: TextView
    private lateinit var proceedButton: Button
    private lateinit var paymentOptionsRadioGroup: RadioGroup

    private lateinit var selectedPaymentOption: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Get the driver details and hiring fee from the intent
        val driverCode = intent.getIntExtra("code", 0)
        val driverName = intent.getStringExtra("name")
        val driverAddress = intent.getStringExtra("address")
        val driverSalary = intent.getStringExtra("salary")
        val hiringFee = intent.getStringExtra("hiringFee")

        // Initialize views
        driverCodeTextView = findViewById(R.id.driverCode)
        driverNameTextView = findViewById(R.id.driverName)
        driverAddressTextView = findViewById(R.id.driverAddress)
        driverSalaryTextView = findViewById(R.id.driverSalary)
        hiringFeeTextView = findViewById(R.id.hiringFee)
        proceedButton = findViewById(R.id.proceedButton)
        paymentOptionsRadioGroup = findViewById(R.id.paymentOptionsRadioGroup)

        // Populate driver details
        driverCodeTextView.text = "Driver Code: $driverCode"
        driverNameTextView.text = "Name: $driverName"
        driverAddressTextView.text = "Address: $driverAddress"
        driverSalaryTextView.text = "Salary Demanded:  ₹$driverSalary"

        // Set hiring fee
        hiringFeeTextView.text = "Hiring Fee:  ₹$hiringFee"

        // Set click listener for Proceed button
        proceedButton.setOnClickListener {
            val selectedPaymentOptionId = paymentOptionsRadioGroup.checkedRadioButtonId
            selectedPaymentOption =
                findViewById<RadioButton>(selectedPaymentOptionId).text.toString()

            // Start payment activity based on the selected payment option
          /*  when (selectedPaymentOption) {
                "UPI" -> startUpiPaymentActivity(hiringFee!!)
                "Card" -> startCardPaymentActivity(hiringFee!!)
                "Netbanking" -> startNetbankingPaymentActivity(hiringFee!!)
            }*/
        }
    }


/*    private fun startUpiPaymentActivity(hiringFee: String) {
        // Launch UPI payment activity
        val intent = Intent(this, UpiPaymentActivity::class.java)
        intent.putExtra("hiringFee", hiringFee)
        startActivity(intent)
    }

    private fun startCardPaymentActivity(hiringFee: String) {
        // Launch Card payment activity
        val intent = Intent(this, CardPaymentActivity::class.java)
        intent.putExtra("hiringFee", hiringFee)
        startActivity(intent)
    }

    private fun startNetbankingPaymentActivity(hiringFee: String) {
        // Launch Netbanking payment activity
        val intent = Intent(this, NetbankingPaymentActivity::class.java)
        intent.putExtra("hiringFee", hiringFee)
        startActivity(intent)
    }*/

}

