package com.omnyom.yumyum.model.login

class LoginRequest(private val email: String, private val password: String) {
    fun get() : HashMap<String, String> {
        var loginInfo = HashMap<String, String>()
        loginInfo.put("email", email)
        loginInfo.put("password", password)

        return loginInfo
    }
}
