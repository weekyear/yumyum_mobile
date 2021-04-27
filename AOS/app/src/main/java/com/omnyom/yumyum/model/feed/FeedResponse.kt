package com.omnyom.yumyum.model.feed

data class FeedResponse(
    val data: Data,
    val message: String,
    val status: String
)

data class User(
        val email: String,
        val introduction: String,
        val nickname: String,
        val profilePath: String,
        val userId: Int
)

data class Data(
        val user: User
)