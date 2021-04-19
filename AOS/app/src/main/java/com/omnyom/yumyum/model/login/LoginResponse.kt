package com.omnyom.yumyum.model.login

data class LoginResponse (val status: String, val message: String, val data: Data)

data class Data(val userResponse: User, val token: String)

data class User(
        val id : Long,
        val email : String,
        val nickname : String,
        val introduction : String,
        val role : String
)