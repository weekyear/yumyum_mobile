package com.omnyom.yumyum.model.like

class LikeRequest (private val feedId: Int, private val userId: Int) {
    fun get() : HashMap<String, Int> {
        return hashMapOf(
                "feedId" to feedId,
                "userId" to userId
        )

    }
}

