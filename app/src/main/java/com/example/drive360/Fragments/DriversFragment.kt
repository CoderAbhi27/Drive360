package com.example.drive360.Fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.drive360.Activity.DriverProfileActivity
import com.example.drive360.Activity.ExploreDrivers
import com.example.drive360.DataClass.Driver
import com.google.firebase.database.*
import com.example.sagarmiles.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class DriversFragment : androidx.fragment.app.Fragment(R.layout.fragment_drivers) {

    private lateinit var dbref : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drivers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val truckDriversButton : LinearLayout = view.findViewById(R.id.truckDriversButton)
        val busDriversButton : LinearLayout = view.findViewById(R.id.busDriversButton)
       // val wheelerDriversButton : LinearLayout = view.findViewById(R.id.wheelerDriversButton)
        val heavyDriversButton : LinearLayout = view.findViewById(R.id.heavyDriversButton)
        val carDriversButton : LinearLayout = view.findViewById(R.id.carDriversButton)
        val searchByCodeButton : FloatingActionButton = view.findViewById(R.id.searchByCode)


        truckDriversButton.setOnClickListener{
            val intent = Intent(activity, ExploreDrivers::class.java)
            intent.putExtra("DriverType", "truck")
            startActivity(intent)
        }

        busDriversButton.setOnClickListener{
            val intent = Intent(activity, ExploreDrivers::class.java)
            intent.putExtra("DriverType", "bus")
            startActivity(intent)
        }

        /*wheelerDriversButton.setOnClickListener{
            val intent = Intent(activity, ExploreDrivers::class.java)
            intent.putExtra("DriverType", "3wheeler")
            startActivity(intent)
        }*/

        heavyDriversButton.setOnClickListener{
            val intent = Intent(activity, ExploreDrivers::class.java)
            intent.putExtra("DriverType", "heavy")
            startActivity(intent)
        }

        carDriversButton.setOnClickListener{
            val intent = Intent(activity, ExploreDrivers::class.java)
            intent.putExtra("DriverType", "car")
            startActivity(intent)
        }


        searchByCodeButton.setOnClickListener{
            val builder : AlertDialog.Builder = AlertDialog.Builder(requireActivity())
            val dialogView: View = LayoutInflater.from(activity).inflate(R.layout.dialog_search_by_code, null)
            builder.setTitle("SEARCH DRIVERS")
            builder.setView(dialogView)

            builder.setPositiveButton("Search", DialogInterface.OnClickListener{ dialog, which->
                val etCode: EditText = dialogView.findViewById(R.id.etCode)
                val code = etCode.text.toString()
                val driverType : String? = when(code[0]-'0'){
                    1 -> "truck"
                    2 -> "bus"
                    //3 -> "3wheeler"
                    4 -> "heavy"
                    5 -> "car"
                    else -> null
                }

                if(driverType.isNullOrEmpty()){
                    Toast.makeText(activity, "Wrong code entered!", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }

                dbref = FirebaseDatabase.getInstance().getReference("drivers").child(driverType).child(code)
                dbref.addListenerForSingleValueEvent( object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            val driver = snapshot.getValue(Driver::class.java)
                            val intent = Intent(activity, DriverProfileActivity::class.java)
                            intent.putExtra("code", driver!!.code)
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
                        }else{
                            Toast.makeText(activity, "Wrong code entered!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // return
                    }
                })

//                Log.d("driver data2: ", "$driverData")






            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialog, which-> })
//            val alertDialog: AlertDialog = builder.create()
            builder.show()
        }

    }

    /*private fun searchByCode(code: String): Driver? {
        val driverType : String? = when(code[0]-'0'){
            1 -> "truck"
            2 -> "bus"
            3 -> "3wheeler"
            4 -> "heavy"
            5 -> "car"
            else -> null
        }

//        Log.d("driver Type1: ", "$driverType")
        if(driverType.isNullOrEmpty()) return null
        Log.d("driver Type2: ", "$driverType")

        var driverData : Driver? = null

        dbref = FirebaseDatabase.getInstance().getReference("drivers").child(driverType).child(code)
        dbref.addListenerForSingleValueEvent( object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    Log.d("driver Type3: ", "$driverType")
                    driverData = snapshot.getValue(Driver::class.java)
                    Log.d("driver data: ", "$driverData")
                    return
                }
            }

            override fun onCancelled(error: DatabaseError) {
               // return
            }
        })

        Log.d("driver data2: ", "$driverData")
        return driverData
    }*/


}