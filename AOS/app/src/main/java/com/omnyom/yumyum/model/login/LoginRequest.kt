package com.omnyom.yumyum.model.login

class LoginRequest(private val email: String) {
    fun get() : HashMap<String, String> {
        var loginInfo = HashMap<String, String>()
        loginInfo.put("email", email)
        return loginInfo
    }
}
