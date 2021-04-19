package com.omnyom.yumyum.model.feed

import android.net.Uri
import androidx.room.Entity

@Entity
data class FeedModel (
    val feedId : Long,
    val videoPath : Uri,
    val content : String
)
