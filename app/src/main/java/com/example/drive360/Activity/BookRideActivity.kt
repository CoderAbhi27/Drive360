package com.example.drive360.Activity

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.drive360.DataClass.bookRideData
import com.example.sagarmiles.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate

class BookRideActivity : AppCompatActivity() {

    private lateinit var dbref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var cost = 0
    private lateinit var rideData: bookRideData

    private val DRIVER_COST = 3000
    private val CAR_COST = 2000
    private val UPI_ID = "8102823536@ibl"

    private val GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user"
    private val GOOGLE_PAY_REQUEST_CODE = 123
    private var status: String? = null
    private var uri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_ride)

        val etName: EditText = findViewById(R.id.etName)
        val etPhone: EditText = findViewById(R.id.etPhone)
        val etDestination: EditText = findViewById(R.id.etDestination)
        val etPickup: EditText = findViewById(R.id.etPickup)
        val fromDatePicker: DatePicker = findViewById(R.id.datePickerFrom)
        val toDatePicker: DatePicker = findViewById(R.id.datePickerTo)
        val checkCar: CheckBox = findViewById(R.id.chkCar)
        val paymentMethodCash: RadioButton = findViewById(R.id.rbCash)
        val paymentMethodOnline: RadioButton = findViewById(R.id.rbOnline)

        val submitButton : Button = findViewById(R.id.submitButton)

        auth = FirebaseAuth.getInstance()
        val phoneNo = auth.currentUser!!.phoneNumber.toString()
        dbref = FirebaseDatabase.getInstance().getReference("ridesBooked").child(phoneNo).push()

        submitButton.setOnClickListener{
            val fromDate = LocalDate.of(fromDatePicker.year, fromDatePicker.month, fromDatePicker.dayOfMonth).toString()
            val toDate = LocalDate.of(toDatePicker.year, toDatePicker.month, toDatePicker.dayOfMonth).toString()
            var paymentMethod: String?=null
            if(paymentMethodCash.isChecked){
                paymentMethod = "cash"
            }else if(paymentMethodOnline.isChecked){
                paymentMethod = "online"
            }
            if(paymentMethod==null){
                Toast.makeText(this, "Please select Payment method!", Toast.LENGTH_SHORT).show()
            } else {
                cost = DRIVER_COST
                if(!checkCar.isChecked) cost+= CAR_COST
                rideData= bookRideData(
                    etName.text.toString(),
                    etPhone.text.toString(),
                    etDestination.text.toString(),
                    etPickup.text.toString(),
                    fromDate,
                    toDate,
                    checkCar.isChecked,
                    paymentMethod,
                    cost
                )
                val builder = AlertDialog.Builder(this)
                builder.setTitle("ARE YOU SURE?")
                builder.setMessage("Ride cost = ₹$cost\nConfirm to book your ride?")
                builder.setPositiveButton("CONFIRM", DialogInterface.OnClickListener{ dialog, which->
                    if(paymentMethod=="cash"){
                        dbref.setValue(rideData).addOnCompleteListener {
                            Toast.makeText(this, "Ride booked successfully!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }.addOnFailureListener{
                            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        uri = getUpiPaymentUri(etName.text.toString(), UPI_ID, "book your ride", cost.toString())
                        payWithGPay()
//                        onPaymentSuccessful()
                    }
                })
                builder.setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialog, which-> })
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()

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
            Toast.makeText(this@BookRideActivity, "Please Install GPay", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val status = data?.getStringExtra("Status")?.toLowerCase()
        if ((Activity.RESULT_OK == resultCode) && status == "success") {
            Toast.makeText(this@BookRideActivity, "Transaction Successful", Toast.LENGTH_SHORT).show()
            onPaymentSuccessful()
        } else {
            Toast.makeText(this@BookRideActivity, "Transaction Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onPaymentSuccessful(){

        Toast.makeText(this@BookRideActivity, "Payment successful!", Toast.LENGTH_SHORT).show()
        rideData.amountDue = 0
        dbref.setValue(rideData).addOnCompleteListener {
            Toast.makeText(this, "Ride booked successfully!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener{
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }

//        val intent = Intent(applicationContext, MainActivity::class.java)
//        val pi = PendingIntent.getActivity(applicationContext, 0, intent, 0)

//        val sms: SmsManager = SmsManager.getDefault()
//        sms.sendTextMessage("$phoneNo", null, "You have hired driver no. $driverCode by paying a fee of rs$hiringFee. on Drive360", null, null)

        val intent2 = Intent(this@BookRideActivity, MainActivity::class.java)
        startActivity(intent2)
        finish()
    }
}