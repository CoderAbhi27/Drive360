package com.drive.drive360.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.drive.drive360.Activity.BookRideActivity
import com.drive.drive360.R

class OnDemandFragment : androidx.fragment.app.Fragment(R.layout.fragment_on_demand) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_demand, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookRideButton : Button = view.findViewById(R.id.bookRideButton)

        bookRideButton.setOnClickListener{
            val intent = Intent(activity, BookRideActivity::class.java)
            startActivity(intent)
        }
    }
}