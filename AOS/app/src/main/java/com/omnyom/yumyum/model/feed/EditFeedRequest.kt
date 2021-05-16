package com.omnyom.yumyum.model.feed

class EditFeedRequest (
    private val content: String?,
    private val id  : Long,
    private val isCompleted: Boolean,
    private val placeResponse: PlaceRequest?,
    private val score : Int?,
    private val title : String?,
    ) {
    fun get() : HashMap<String, Any?> {
        return hashMapOf(
                "content" to content,
                "id" to id,
                "isCompleted" to isCompleted,
                "placeResponse" to placeResponse,
                "score" to score,
                "title" to title
        )
    }
}