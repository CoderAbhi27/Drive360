package com.example.drive360.Activity

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import com.example.sagarmiles.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.drive360.DataClass.Driver
import kotlin.properties.Delegates

//import com.tournaments.googlepaytest.databinding.ActivityMainBinding


class PaymentActivity : AppCompatActivity() {
    private lateinit var dbref : DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var driverCodeTextView: TextView
    private lateinit var driverNameTextView: TextView
    private lateinit var driverAddressTextView: TextView
    private lateinit var driverSalaryTextView: TextView
    private lateinit var hiringFeeTextView: TextView
    private lateinit var proceedButton: Button
    private lateinit var selectedPaymentOption: String
    private var driverCode by Delegates.notNull<Int>()
    private lateinit var hiringFee : String

    private val GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user"
    private val GOOGLE_PAY_REQUEST_CODE = 123
    private var status: String? = null
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Get the driver details and hiring fee from the intent
        driverCode = intent.getIntExtra("code", 0)
        val driverName = intent.getStringExtra("name")
        val driverAddress = intent.getStringExtra("address")
        val driverSalary = intent.getStringExtra("salary")
        hiringFee = intent.getStringExtra("hiringFee")!!

        // Initialize views
        driverCodeTextView = findViewById(R.id.driverCode)
        driverNameTextView = findViewById(R.id.driverName)
        driverAddressTextView = findViewById(R.id.driverAddress)
        driverSalaryTextView = findViewById(R.id.driverSalary)
        hiringFeeTextView = findViewById(R.id.hiringFee)
        proceedButton = findViewById(R.id.proceedButton)

        driverCodeTextView.text = "Driver Code: $driverCode"
        driverNameTextView.text = "Name: $driverName"
        driverAddressTextView.text = "Address: $driverAddress"
        driverSalaryTextView.text = "Salary Demanded:  ₹$driverSalary"

        hiringFeeTextView.text = "Hiring Fee:  ₹$hiringFee"

        val upiRadioButton : RadioButton = findViewById(R.id.upiOption)


        proceedButton.setOnClickListener {
            if (upiRadioButton.isChecked) {
                uri = getUpiPaymentUri(driverName!!, "916227406346@paytm", "Hire your driver", hiringFee!!)
                payWithGPay()
//                onPaymentSuccessful(driverCode)
            } else {
                Toast.makeText(this@PaymentActivity, "Please select payment method!", Toast.LENGTH_SHORT).show()
            }
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
            Toast.makeText(this@PaymentActivity, "Please Install GPay", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val status = data?.getStringExtra("Status")?.toLowerCase()
        if ((Activity.RESULT_OK == resultCode) && status == "success") {
            Toast.makeText(this@PaymentActivity, "Transaction Successful", Toast.LENGTH_SHORT).show()
            onPaymentSuccessful(driverCode)
        } else {
            Toast.makeText(this@PaymentActivity, "Transaction Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onPaymentSuccessful(driverCode: Int){

        auth = FirebaseAuth.getInstance()
        val phoneNo = auth.currentUser!!.phoneNumber.toString()
        dbref = FirebaseDatabase.getInstance().getReference("driversHired").child(phoneNo).child(driverCode.toString())

        val driverType : String? = when(driverCode/1000){
            1 -> "truck"
            2 -> "bus"
            //3 -> "3wheeler"
            4 -> "heavy"
            5 -> "car"
            else -> null
        }
        val dbref2 = FirebaseDatabase.getInstance().getReference("drivers").child(driverType!!).child(driverCode.toString())
        dbref2.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("data2", "yes2")
                if (snapshot.exists()){
                    Log.i("data2", "yes3")
                    val data = snapshot.getValue(Driver::class.java)
                    dbref.setValue(data)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PaymentActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }

        })

        Toast.makeText(this@PaymentActivity, "Driver Hired!", Toast.LENGTH_SHORT).show()

//        val intent = Intent(applicationContext, MainActivity::class.java)
//        val pi = PendingIntent.getActivity(applicationContext, 0, intent, 0)

//        val sms: SmsManager = SmsManager.getDefault()
//        sms.sendTextMessage("$phoneNo", null, "You have hired driver no. $driverCode by paying a fee of rs$hiringFee. on Drive360", null, null)

        val intent2 = Intent(this@PaymentActivity, MainActivity::class.java)
        startActivity(intent2)
        finish()
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

