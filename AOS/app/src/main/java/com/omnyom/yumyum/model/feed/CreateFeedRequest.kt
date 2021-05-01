package com.omnyom.yumyum.model.feed


class CreateFeedRequest(
        private val content: String,
        private val placeRequest: PlaceRequest,
        private val score : Int,
        private val thumbnailPath : String,
        private val title : String,
        private val userId  : Long,
        private val videoPath : String

) {
    fun get() : HashMap<String, Any> {
        return hashMapOf(
                "content" to content,
                "placeRequest" to placeRequest,
                "score" to score,
                "thumbnailPath" to thumbnailPath,
                "title" to title,
                "userId" to userId,
                "videoPath" to videoPath,
        )
    }
}

data class PlaceRequest(
        val address: String,
        val locationX: Double,
        val locationY: Double,
        val name: String,
        val phone: String
)
