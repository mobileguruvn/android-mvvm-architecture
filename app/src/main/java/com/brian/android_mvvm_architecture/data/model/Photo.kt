package com.brian.android_mvvm_architecture.data.model

import com.brian.android_mvvm_architecture.data.local.database.entity.PhotoEntity

data class Photo(
    val albumId: Int,
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String,
)