package com.drive.drive360.DataClass

data class bookRideData(
    val name: String?="",
    val phone: String?="",
    val destinationAddress: String?="",
    val pickupAddress: String?="",
    val fromDate: String?="",
    val toDate: String?="",
    val checkCar: Boolean=false,
    val paymentMethod: String?="",
    var amountDue: Int=0
)
