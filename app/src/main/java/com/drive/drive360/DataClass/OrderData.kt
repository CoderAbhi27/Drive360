package com.drive.drive360.DataClass

data class OrderData(
    val name: String?="",
    val phone: String?="",
    val company: String?="",
    val noOfDevices: Int=0,
    val deliveryAddress: String?=""
)
