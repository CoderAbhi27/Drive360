package com.example.drive360.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drive360.Adapters.DriverAdapter
import com.example.drive360.DataClass.Driver
import com.example.sagarmiles.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class YourDriversActivity : AppCompatActivity(), ItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DriverAdapter
    private var dataList : ArrayList<Driver> = ArrayList<Driver>()
    private var codeList : ArrayList<String> = ArrayList<String>()
    private lateinit var dbref : DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_drivers)

        recyclerView = findViewById(R.id.recyclerViewHired)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.setHasFixedSize(true)

        auth = FirebaseAuth.getInstance()
        val phoneNo = auth.currentUser!!.phoneNumber.toString()
        dbref = FirebaseDatabase.getInstance().getReference("driversHired").child(phoneNo)
        dbref.addValueEventListener( object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                codeList.clear()
                if(snapshot.exists()){
                    for(dataSnap in snapshot.children){
                        val code = dataSnap.key ?: continue
                        codeList.add(code)
                        Log.i("data2", "yes")

                    }
                    for(code in codeList){
                        val driverType : String = when(code[0]-'0'){
                            1 -> "truck"
                            2 -> "bus"
                            3 -> "3wheeler"
                            4 -> "heavy"
                            5 -> "car"
                            else -> null
                        } ?: continue

                        val dbref2 = FirebaseDatabase.getInstance().getReference("drivers").child(driverType).child(code)
                        dbref2.addListenerForSingleValueEvent( object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                Log.i("data2", "yes2")
                                if (snapshot.exists()){
                                    Log.i("data2", "yes3")
                                    val data = snapshot.getValue(Driver::class.java)
                                    dataList.add(data!!)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@YourDriversActivity, error.toString(), Toast.LENGTH_SHORT).show()
                            }

                        })
                    }
                    Log.i("data3", "$dataList[0]")
                    adapter = DriverAdapter(dataList, this@YourDriversActivity)
                    recyclerView.adapter = adapter

                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@YourDriversActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, HiredDriverProfileActivity::class.java)
        val driver = dataList[position]

        intent.putExtra("code",driver.code)
        intent.putExtra("name",driver.name)
        intent.putExtra("image",driver.image)
        intent.putExtra("phoneNo",driver.phoneNo)
        intent.putExtra("age",driver.age)
        intent.putExtra("address", driver.address)
        intent.putExtra("experience",driver.experience)
        intent.putExtra("salaryDemanded",driver.salaryDemanded)
        intent.putExtra("certificate",driver.certificate)
        startActivity(intent)
    }
}