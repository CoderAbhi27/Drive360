package com.example.drive360.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.drive360.Activity.YourDriversActivity
import com.example.drive360.DataClass.User
import com.example.sagarmiles.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


class ProfileFragment : androidx.fragment.app.Fragment(R.layout.fragment_profile) {

    private lateinit var dbref : DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val phoneNo = auth.currentUser!!.phoneNumber.toString()
        dbref = FirebaseDatabase.getInstance().getReference("users").child(phoneNo)

        val profilePic: ImageView = view.findViewById(R.id.profilePicture)
        val editProfilePicButton: ImageButton = view.findViewById(R.id.editProfilePicture)
        val myNameTv : TextView = view.findViewById(R.id.myNameTextView)
        val phoneNoTv : TextView = view.findViewById(R.id.myPhnoTextView)
        val locationTv : TextView = view.findViewById(R.id.locationTextView)
        val ageTv : TextView = view.findViewById(R.id.userAgeTextView)

        Log.i("recyclerData","${dbref.key.toString()}")

        dbref.addListenerForSingleValueEvent( object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("recyclerData","yes2")
                if(snapshot.exists()){
                    Log.i("recyclerData","yes")
                    val userData= snapshot.getValue(User::class.java)
                    myNameTv.text= "Name: $userData.name"
                    phoneNoTv.text= "Phone No.: $userData.phoneNo"
                    locationTv.text= "Location: $userData.location"
                    ageTv.text= "Age: $userData.age"
                    if(userData!!.profilePic!="none") Picasso.get().load(userData.profilePic).into(profilePic)
                }
                else Log.i("recyclerData","no")
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT)
            }
        })

        val yourDriversButton: Button = view.findViewById(R.id.yourDriversButton)

        yourDriversButton.setOnClickListener{
            val intent = Intent(activity, YourDriversActivity::class.java)
            startActivity(intent)
        }

    }
}