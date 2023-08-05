package com.example.drive360.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.drive360.Activity.FuelTrackingOrderActivity
import com.example.sagarmiles.R

class FuelTrackingFragment : androidx.fragment.app.Fragment(R.layout.fragment_fuel_tracking) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fuel_tracking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderNowButton : Button = view.findViewById(R.id.orderNowButton)

        orderNowButton.setOnClickListener{
            val intent = Intent(activity, FuelTrackingOrderActivity::class.java)
            startActivity(intent)
        }
    }
}