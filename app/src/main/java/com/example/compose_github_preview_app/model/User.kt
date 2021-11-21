package com.example.compose_github_preview_app.model

import com.google.gson.annotations.SerializedName

data class User(
    val name: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    val company: String?,
    val blog: String,
    val location: String,
    val bio: String?,
    @SerializedName("twitter_user_name")
    val twitterUserName: String,
)