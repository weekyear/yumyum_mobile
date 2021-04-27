package com.omnyom.yumyum.model.feed

data class AllFeedResponse(
    val data: List<Data>,
    val message: String,
    val status: String
)

data class Data(
        val content: String,
        val feedId: Int,
        val isLike: Boolean,
        val likeCount: Int,
        val placeId: Int,
        val score: Int,
        val thumnailPath: String,
        val title: String,
        val userId: Int,
        val videoPath: String
)