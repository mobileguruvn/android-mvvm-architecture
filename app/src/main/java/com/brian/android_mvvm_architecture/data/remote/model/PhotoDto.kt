package com.brian.android_mvvm_architecture.data.remote.model

import com.google.gson.annotations.SerializedName

data class PhotoDto(
    @SerializedName("albumId") val albumId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String,
)