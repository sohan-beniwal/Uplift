package com.example.uplift.dataclass

data class User(
    val name: String ?= null,
    val mobile: String ?= null,
    val city: String ?= null,
    val address: String ?= null,
    val admin: Boolean = false,
    val longitude: Double? = null,
    val latitude: Double? = null
)
