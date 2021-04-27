package com.omnyom.yumyum.model.login

data class LoginResponse (val status: String, val message: String, val data: Data)

data class Data(val user: User)

data class User(
        val userId : Long,
        val email : String,
        val nickname : String,
        val introduction : String,
        val profilePath : String
)