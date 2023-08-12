package com.example.drive360.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drive360.Adapters.DriverAdapter
import com.example.drive360.Adapters.OrdersAdapter
import com.example.drive360.DataClass.Driver
import com.example.drive360.DataClass.OrderData
import com.example.sagarmiles.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class YourOrdersActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrdersAdapter
    private var dataList : ArrayList<OrderData> = ArrayList<OrderData>()
    private lateinit var dbref : DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_orders)

        recyclerView = findViewById(R.id.recyclerViewOrders)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        recyclerView.setHasFixedSize(true)

        auth = FirebaseAuth.getInstance()
        val phoneNo = auth.currentUser!!.phoneNumber.toString()
        dbref = FirebaseDatabase.getInstance().getReference("ordersBooked").child(phoneNo)
        dbref.addValueEventListener( object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                if(snapshot.exists()){
                    for(dataSnap in snapshot.children){
                        val data= dataSnap.getValue(OrderData::class.java)
                        dataList.add(data!!)

                    }
                    adapter = OrdersAdapter(dataList, this@YourOrdersActivity)
                    recyclerView.adapter = adapter

                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@YourOrdersActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })

    }
}