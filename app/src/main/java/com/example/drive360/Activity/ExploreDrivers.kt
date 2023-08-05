package com.example.drive360.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drive360.Adapters.DriverAdapter
import com.example.drive360.DataClass.Driver
import com.google.firebase.database.*
import com.example.sagarmiles.R

class ExploreDrivers : AppCompatActivity() , ItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DriverAdapter
    private var dataList : ArrayList<Driver> = ArrayList<Driver>()
    private lateinit var dbref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore_drivers)

        val driverType = intent.getStringExtra("DriverType")

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.setHasFixedSize(true)

//        getDriverData(driverType!!)

        dbref = FirebaseDatabase.getInstance().getReference("drivers").child(driverType!!)
        dbref.addValueEventListener( object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                if(snapshot.exists()){
                    for(dataSnap in snapshot.children){
                        if(dataSnap.key=="lastCode") continue
                        val data = dataSnap.getValue(Driver::class.java)
                        dataList.add(data!!)
                    }

                    adapter = DriverAdapter(dataList, this@ExploreDrivers)
                    recyclerView.adapter = adapter

                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ExploreDrivers, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })


    }

    override fun onItemClick(position: Int) {
        // Handle item click event here
        val intent = Intent(this, DriverProfileActivity::class.java)
        val driver = dataList[position]

        intent.putExtra("code",driver.code)
        intent.putExtra("name",driver.name)
        intent.putExtra("image",driver.image)
        intent.putExtra("phoneNo",driver.phoneNo)
        intent.putExtra("age",driver.age)
        intent.putExtra("address", driver.address)
        intent.putExtra("experience",driver.experience)
        intent.putExtra("salaryDemanded",driver.salaryDemanded)
        intent.putExtra("hiringFee",driver.hiringFee)
        intent.putExtra("certificate",driver.certificate)

        startActivity(intent)
    }

/*    // This is a placeholder function to simulate fetching data from a database
    private fun getDriverData(drivertype: String){

        dbref = FirebaseDatabase.getInstance().getReference(drivertype)
        dbref.addValueEventListener( object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                if(snapshot.exists()){
                    for(dataSnap in snapshot.children){
                        val data = dataSnap.getValue(Driver::class.java)
                        dataList.add(data!!)
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ExploreDrivers, error.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }*/
}

interface ItemClickListener {
    fun onItemClick(position: Int)
}