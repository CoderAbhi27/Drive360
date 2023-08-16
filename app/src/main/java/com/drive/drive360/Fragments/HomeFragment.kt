package com.drive.drive360.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.drive.drive360.Activity.ExploreDrivers
import com.drive.drive360.R

class HomeFragment : androidx.fragment.app.Fragment(R.layout.fragment_home) {

    private lateinit var lottieAnimationView:LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lottieAnimationView=view.findViewById(R.id.lottieAnimationView3)
        lottieAnimationView.setMinAndMaxProgress(0.0f,0.5f)

        val tvTruck: TextView = view.findViewById(R.id.tvTruck)
        val tvBus: TextView = view.findViewById(R.id.tvBus)
        val tvHeavy: TextView = view.findViewById(R.id.tvHeavy)
        val tvCar: TextView = view.findViewById(R.id.tvCar)

        tvTruck.setOnClickListener {
            val intent = Intent(activity, ExploreDrivers::class.java)
            intent.putExtra("DriverType", "truck")
            startActivity(intent)
        }
        tvBus.setOnClickListener {
            val intent = Intent(activity, ExploreDrivers::class.java)
            intent.putExtra("DriverType", "bus")
            startActivity(intent)
        }
        tvHeavy.setOnClickListener {
            val intent = Intent(activity, ExploreDrivers::class.java)
            intent.putExtra("DriverType", "heavy")
            startActivity(intent)
        }
        tvCar.setOnClickListener {
            val intent = Intent(activity, ExploreDrivers::class.java)
            intent.putExtra("DriverType", "car")
            startActivity(intent)
        }
    }


}