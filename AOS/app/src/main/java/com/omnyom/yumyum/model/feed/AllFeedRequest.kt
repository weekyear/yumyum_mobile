package com.omnyom.yumyum.model.feed


data class AllFeedRequest(private val userId: Long) {
    fun get() : HashMap<String, Long> {
        var getFeedInfo = HashMap<String, Long>()
        getFeedInfo.put("userId", userId)

        return getFeedInfo
    }
}