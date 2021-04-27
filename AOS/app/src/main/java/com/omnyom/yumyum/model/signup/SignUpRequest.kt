package com.omnyom.yumyum.model.signup

class SignUpRequest(private val email: String,
                    private val nickname: String,
                    private val introduction: String) {
    fun get() : HashMap<String, String> {
        var signUpInfo = HashMap<String, String>()
        signUpInfo.put("email", email)
        signUpInfo.put("nickname", nickname)
        signUpInfo.put("introduction", introduction)
        signUpInfo.put("profilePath", "profilePath")
        return signUpInfo
    }
}
