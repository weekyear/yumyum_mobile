package com.omnyom.yumyum.model.feed

import java.io.Serializable

data class FeedResponse(
    val `data`: List<FeedData>,
    val message: String,
    val status: String
)

data class FeedData(
        val content: String,
        val createdDate: String,
        val id: Int,
        val isLike: Boolean,
        val likeCount: Int,
        val modifiedDate: String,
        val place: Place,
        val thumbnailPath: String,
        val title: String,
        val user: User,
        val videoPath: String
) : Serializable

data class User(
        val createdDate: String,
        val email: String,
        val id: Int,
        val introduction: String,
        val modifiedDate: String,
        val nickname: String,
        val profilePath: String
) : Serializable


data class Place(
        val address: String,
        val id: Int,
        val locationX: Double,
        val locationY: Double,
        val name: String,
        val phone: String
) : Serializable
