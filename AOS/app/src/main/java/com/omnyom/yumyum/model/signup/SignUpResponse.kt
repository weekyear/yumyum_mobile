package com.omnyom.yumyum.model.signup

data class SignUpResponse(
    val `data`: Data,
    val message: String,
    val status: String
)

data class Data(
        val email: String,
        val id: Int,
        val introduction: String,
        val nickname: String,
        val profilePath: String
)