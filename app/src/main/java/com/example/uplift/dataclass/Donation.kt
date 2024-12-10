package com.example.uplift.dataclass

data class Donation(
    val category: String = "",
    val item: String = "",
    val quantity: String = "",
    val info: String = "",
    val day: String = "",
    val time: String = "",
    val userId: String? = null,
    val userName: String? = null,
    val userPhoneNumber: String? = null,
    val userAddress: String? = null
)


