package com.example.drive360.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drive360.Adapters.OrdersAdapter
import com.example.drive360.Adapters.RidesAdapter
import com.example.drive360.DataClass.OrderData
import com.example.drive360.DataClass.bookRideData
import com.example.sagarmiles.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class YourRidesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RidesAdapter
    private var dataList : ArrayList<bookRideData> = ArrayList<bookRideData>()
    private lateinit var dbref : DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_rides)

        recyclerView = findViewById(R.id.recyclerViewRides)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        recyclerView.setHasFixedSize(true)

        auth = FirebaseAuth.getInstance()
        val phoneNo = auth.currentUser!!.phoneNumber.toString()
        dbref = FirebaseDatabase.getInstance().getReference("ridesBooked").child(phoneNo)
        dbref.addValueEventListener( object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                if(snapshot.exists()){
                    for(dataSnap in snapshot.children){
                        val data= dataSnap.getValue(bookRideData::class.java)
                        dataList.add(data!!)

                    }
                    adapter = RidesAdapter(dataList, this@YourRidesActivity)
                    recyclerView.adapter = adapter

                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@YourRidesActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}