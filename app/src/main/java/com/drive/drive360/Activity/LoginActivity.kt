package com.drive.drive360.Activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.*
import java.util.concurrent.TimeUnit
import com.google.android.material.textfield.TextInputEditText
import com.drive.drive360.R

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbref: DatabaseReference
    private var storedVerificationId : String?=null
    private lateinit var phoneNo : String
    private lateinit var pd : ProgressDialog
    private lateinit var editTextOTP : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_phone)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        dbref = FirebaseDatabase.getInstance().getReference("users")

        val buttonGetOTP: Button= findViewById(R.id.buttonGetOTP)
        val buttonVerifyOTP : Button= findViewById(R.id.buttonVerifyOTP)
        val editTextCountryCode : EditText= findViewById(R.id.editTextCountryCode)
        val editTextMobile : TextInputEditText= findViewById(R.id.editTextMobile)
        editTextOTP = findViewById(R.id.editTextOTP)

        buttonGetOTP.setOnClickListener {
            pd= ProgressDialog(this)
            pd.setMessage("Verifying Phone no...")
            pd.show()

            if(editTextMobile.text.toString().length<10){
                pd.dismiss()
                Toast.makeText(this,"Please enter a valid phone number", Toast.LENGTH_LONG).show()
            }
            else{
                phoneNo = editTextCountryCode.text.toString() + editTextMobile.text.toString()
                sendVerificationCode(phoneNo)
            }
        }

        buttonVerifyOTP.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
            val otp = editTextOTP.text.toString()
            // Here, you should have stored the verificationId sent to the sendVerificationCode function.
            // Let's assume it's stored in a variable called storedVerificationId

            if(storedVerificationId.isNullOrEmpty()){
                Toast.makeText(this,"Get OTP first", Toast.LENGTH_SHORT).show()
            }
            else{
                pd = ProgressDialog(this)
                pd.setMessage("Verifying OTP...")
                pd.show()
                val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, otp)
                signInWithPhoneAuthCredential(credential, phoneNo)
            }


        }

    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    pd.dismiss()
                    pd = ProgressDialog(this@LoginActivity)
                    pd.setMessage("Verifying OTP...")
                    pd.show()
                    signInWithPhoneAuthCredential(phoneAuthCredential, number)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // this method is called when verification failed
                    pd.dismiss()
                    Toast.makeText(this@LoginActivity, e.toString(), Toast.LENGTH_LONG).show()
                    Log.i("fberror",e.toString())
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    // The SMS verification code has been sent to the provided phone number
                    pd.dismiss()
                    editTextOTP.requestFocus()
                    Toast.makeText(this@LoginActivity, "OTP sent", Toast.LENGTH_SHORT).show()
                    storedVerificationId = verificationId
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, number: String ) {
        Log.i("signInData","signing in..")
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Log.i("signInData","sign in success")
                    dbref.child(number).addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(!snapshot.exists()){
                                Log.i("signInData","no data")
                                val intent = Intent(this@LoginActivity, CreateProfile::class.java)
                                intent.putExtra("Phone", number)
                                pd.dismiss()
                                startActivity(intent)
                                finish()
                            }else{
                                Log.i("signInData","yes data")
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                pd.dismiss()
                                startActivity(intent)
                                finish()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@LoginActivity, "Failed to sign in", Toast.LENGTH_LONG)
                                .show()
                            pd.dismiss()
                        }

                    })



                } else {
                    // Sign in failed
                    Toast.makeText(this, "Wrong OTP entered!", Toast.LENGTH_LONG)
                        .show()
                    pd.dismiss()
                }
            }
    }

    override fun onStart() {
        super.onStart()

        Log.i("signInData","onStart")
        if(auth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }




}