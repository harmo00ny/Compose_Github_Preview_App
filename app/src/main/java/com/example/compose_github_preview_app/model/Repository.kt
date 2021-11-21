package com.example.compose_github_preview_app.model

import com.google.gson.annotations.SerializedName

data class Repository (
    val id: String,
    val name: String,
    @SerializedName("full_name")
    val fullName: String,
    val description: String?,
    val url: String,
)