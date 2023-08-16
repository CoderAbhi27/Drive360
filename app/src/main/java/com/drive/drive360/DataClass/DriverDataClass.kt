package com.drive.drive360.DataClass

data class Driver(
    val code: Int = 0,
    val type: String?=null,
    val name: String?=null,
    val phoneNo: String?=null,
    val age: Int = 0,
    val image: String?=null,
    val address: String?=null,
    val experience: Int=0,
    val salaryDemanded: String?=null,
    val hiringFee: String?=null,
    val certificate: String?=null
)
