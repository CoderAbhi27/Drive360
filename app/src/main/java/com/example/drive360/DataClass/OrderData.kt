package com.example.drive360.DataClass

import android.location.Address

data class OrderData(
    val name: String?="",
    val phone: String?="",
    val company: String?="",
    val noOfDevices: Int=0,
    val deliveryAddress: String?=""
)
