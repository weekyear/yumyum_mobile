package com.omnyom.yumyum.model.eureka

data class Chat(
    val userId: Int,
    val geohash: String,
    val lat: Double,
    val lng: Double,
    val message: String
)