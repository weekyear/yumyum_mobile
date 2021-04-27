package com.omnyom.yumyum.model.feed

data class AllFeedResponse(
        val `data`: List<FeedData>,
        val message: String,
        val status: String
)

data class FeedData(
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