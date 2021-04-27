package com.omnyom.yumyum.model.feed


class CreateFeedRequest(
        private val content: String,
        private val placeRequest: PlaceRequest,
        private val score : Int,
        private val thumbnailPath : String,
        private val title : String,
        private val userId  : Int,
        private val videoPath : String

) {
    fun get() : HashMap<String, Any> {
        var createFeedInfo = HashMap<String, Any>()
        createFeedInfo.put("content", content)
        createFeedInfo.put("placeRequest", placeRequest)
        createFeedInfo.put("score", score)
        createFeedInfo.put("thumbnailPath", thumbnailPath)
        createFeedInfo.put("title", title)
        createFeedInfo.put("userId", userId)
        createFeedInfo.put("videoPath", videoPath)
        return createFeedInfo
    }
}

data class PlaceRequest(
        val address: String,
        val locationX: Double,
        val locationY: Double,
        val name: String,
        val phone: String
)
