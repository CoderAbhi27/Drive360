package com.drive.drive360.Activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.drive.drive360.Fragments.*
import com.drive.drive360.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        dbref = FirebaseDatabase.getInstance().getReference("users")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(R.layout.activity_main)

        replaceFragment(HomeFragment())

        val bottomNavigationView : BottomNavigationView = findViewById(R.id.BottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener {

            if(it.itemId == R.id.Home){
                replaceFragment(HomeFragment())
            }
            if (it.itemId == R.id.Drivers){
                replaceFragment(DriversFragment())
            }
            if (it.itemId == R.id.OnDemand){
                replaceFragment(OnDemandFragment())
            }
            if (it.itemId == R.id.FuelTracking){
                replaceFragment(FuelTrackingFragment())
            }
            if (it.itemId == R.id.Profile){
                replaceFragment(ProfileFragment())
            }
            return@setOnItemSelectedListener true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction : FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame, fragment)
        fragmentTransaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu!!.add("Log Out")
        menu.add("Delete Account")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.title?.equals("Log Out") == true){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("LOG OUT")
            builder.setMessage("Are you sure?")
            builder.setPositiveButton("LOG OUT", DialogInterface.OnClickListener{ dialog, which->
                firebaseAuth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialog, which-> })
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
        if(item.title?.equals("Delete Account") == true){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("DELETE YOUR ACCOUNT?")
            builder.setMessage("This action will permanently delete your acount and all the data associated with it! ARE YOUR SURE?")
            builder.setPositiveButton("DELETE MY ACCOUNT", DialogInterface.OnClickListener{ dialog, which->
                val phNo = firebaseAuth.currentUser!!.phoneNumber.toString()
                dbref.child(phNo).removeValue()
                firebaseAuth.currentUser?.delete()
                firebaseAuth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialog, which-> })
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
        if(item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}