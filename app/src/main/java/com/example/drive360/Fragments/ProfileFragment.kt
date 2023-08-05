package com.example.drive360.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.drive360.Activity.YourDriversActivity
import com.example.sagarmiles.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference


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

        val yourDriversButton: Button = view.findViewById(R.id.yourDriversButton)

        yourDriversButton.setOnClickListener{
            val intent = Intent(activity, YourDriversActivity::class.java)
            startActivity(intent)
        }

    }
}