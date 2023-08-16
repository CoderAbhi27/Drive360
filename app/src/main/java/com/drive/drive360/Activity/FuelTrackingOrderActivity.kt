package com.drive.drive360.Activity

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.drive.drive360.DataClass.OrderData
import com.drive.drive360.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FuelTrackingOrderActivity : AppCompatActivity() {

    private lateinit var ftCompany: AutoCompleteTextView

    private lateinit var dbref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var cost = 0
    private lateinit var orderData: OrderData;

    private val PER_ORDER_COST = 1000
    private val UPI_ID = "8102823536@ibl"

    private val GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user"
    private val GOOGLE_PAY_REQUEST_CODE = 123
    private var status: String? = null
    private var uri: Uri? = null

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

        val etName: EditText = findViewById(R.id.ftName)
        val etPhone: EditText = findViewById(R.id.ftPhone)
        val etCompany: EditText = findViewById(R.id.ftCompany)
        val etNoOfDevices: EditText = findViewById(R.id.ftNoOfDevices)
        val etDeliveryAddress: EditText = findViewById(R.id.ftDeliveryAddress)
        val submitButton: Button = findViewById(R.id.submitButton)

        auth = FirebaseAuth.getInstance()
        val phoneNo = auth.currentUser!!.phoneNumber.toString()
        dbref = FirebaseDatabase.getInstance().getReference("ordersBooked").child(phoneNo).push()
        submitButton.setOnClickListener{
            val noOfDevices = etNoOfDevices.text.toString().toInt()
            cost= noOfDevices*PER_ORDER_COST
            val builder = AlertDialog.Builder(this)
            builder.setTitle("BOOK YOUR FLS ORDER?")
            builder.setMessage("Order cost = ₹$cost")

            builder.setPositiveButton("CONFIRM", DialogInterface.OnClickListener{ dialog, which->
                orderData = OrderData(etName.text.toString(), etPhone.text.toString(), etCompany.text.toString(), noOfDevices, etDeliveryAddress.text.toString())
                uri = getUpiPaymentUri(etName.text.toString(), UPI_ID, "book your FLS order", cost.toString())
                payWithGPay()
//                onPaymentSuccessful()

            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialog, which-> })
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()

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
            Toast.makeText(this@FuelTrackingOrderActivity, "Please Install GPay", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val status = data?.getStringExtra("Status")?.toLowerCase()
        if ((AppCompatActivity.RESULT_OK == resultCode) && status == "success") {
            Toast.makeText(this@FuelTrackingOrderActivity, "Transaction Successful", Toast.LENGTH_SHORT).show()
            onPaymentSuccessful()
        } else {
            Toast.makeText(this@FuelTrackingOrderActivity, "Transaction Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onPaymentSuccessful(){

        Toast.makeText(this@FuelTrackingOrderActivity, "Payment successful!", Toast.LENGTH_SHORT).show()

//        val intent = Intent(applicationContext, MainActivity::class.java)
//        val pi = PendingIntent.getActivity(applicationContext, 0, intent, 0)
//        val sms: SmsManager = SmsManager.getDefault()
//        sms.sendTextMessage("$phoneNo", null, "You have hired driver no. $driverCode by paying a fee of rs$hiringFee. on Drive360", null, null)

        dbref.setValue(orderData).addOnCompleteListener {
            Toast.makeText(this, "Order booked successfully!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener{
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }

        val intent2 = Intent(this@FuelTrackingOrderActivity, MainActivity::class.java)
        startActivity(intent2)
        finish()
    }
}