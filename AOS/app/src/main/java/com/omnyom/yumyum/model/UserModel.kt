package com.omnyom.yumyum.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserModel(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val googleEmail : String,
    val user_key: String,
    val nickname: String,
    val updated_at: String,
    val created_at: String,
    val avatar: String,
)
