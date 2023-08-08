package com.example.drive360.Fragments

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.drive360.Activity.YourDriversActivity
import com.example.sagarmiles.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso


class ProfileFragment : androidx.fragment.app.Fragment(R.layout.fragment_profile) {

    private lateinit var dbref : DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var pp: String
    private lateinit var imageUri: Uri
    private lateinit var storage: FirebaseStorage
//    private lateinit var selectImageLauncher: ActivityResultLauncher<String>

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
        storage = FirebaseStorage.getInstance()

        val profilePic: ImageView = view.findViewById(R.id.profilePicture)
        val editProfilePicButton: ImageButton = view.findViewById(R.id.editProfilePicture)
        val myNameTv : TextView = view.findViewById(R.id.myNameTextView)
        val phoneNoTv : TextView = view.findViewById(R.id.myPhnoTextView)
        val locationTv : TextView = view.findViewById(R.id.locationTextView)
        val ageTv : TextView = view.findViewById(R.id.userAgeTextView)

        val selectImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                // Handle image URI selection
                if (uri != null) {
                    imageUri = uri
                    Log.i("uril","yes")
                    profilePic.setImageURI(uri)
                }
            }

        dbref.addValueEventListener( object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    Log.i("recyclerData","yes")
                    //val userData= snapshot.getValue(User::class.java)
                    myNameTv.text= "Name: ${snapshot.child("name").getValue()}"
                    phoneNoTv.text= "Phone No.: ${snapshot.child("phoneNo").getValue()}"
                    locationTv.text= "Location: ${snapshot.child("location").getValue()}"
                    ageTv.text= "Age: ${snapshot.child("age").getValue()}"
                    pp = snapshot.child("profilePic").getValue().toString()
                    if(pp!="none") Picasso.get().load(pp).into(profilePic)
                    else profilePic.setImageResource(R.drawable.profile)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT)
            }
        })

        profilePic.setOnClickListener{
            val builder = AlertDialog.Builder(requireActivity())
            if(pp=="none"){
                builder.setTitle("ADD PROFILE PICTURE?")
                builder.setPositiveButton("YES", DialogInterface.OnClickListener{ dialog, which->
                    selectImageLauncher.launch("image/*")
                    val imageRef = storage.reference.child("UserData").child("images/${imageUri.lastPathSegment}")
                    imageRef.putFile(imageUri)
                        .addOnSuccessListener {
                            imageRef.downloadUrl.addOnSuccessListener { imageUrl ->
                                dbref.child("profilePic").setValue(imageUrl)
                            }
                        }
                })
                builder.setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialog, which-> })
            }
            else{
                builder.setTitle("EDIT PROFILE PICTURE")
                builder.setMessage("What do you want to do?")
                builder.setPositiveButton("Delete", DialogInterface.OnClickListener{ dialog, which->
                    dbref.child("profilePic").setValue("none")
                })
                builder.setPositiveButton("Change", DialogInterface.OnClickListener{ dialog, which->
                    updateProfilePic()
                })
                builder.setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialog, which-> })
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }

        val yourDriversButton: Button = view.findViewById(R.id.yourDriversButton)

        yourDriversButton.setOnClickListener{
            val intent = Intent(activity, YourDriversActivity::class.java)
            startActivity(intent)
        }



    }


    private fun updateProfilePic() {
      /*  val selectImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                // Handle image URI selection
                if (uri != null) {
                    imageUri = uri
                }
            }*/

    }
}