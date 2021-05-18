package com.omnyom.yumyum.model.feed

data class FeedAiResponse(
    val `data`: List<String>,
    val message: String,
    val status: String
)