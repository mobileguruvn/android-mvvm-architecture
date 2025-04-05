package com.brian.android_mvvm_architecture.data.mapper

import com.brian.android_mvvm_architecture.data.local.database.entity.PhotoEntity
import com.brian.android_mvvm_architecture.data.model.FavouritePhoto
import com.brian.android_mvvm_architecture.data.model.Photo
import com.brian.android_mvvm_architecture.data.remote.model.PhotoDto

fun PhotoDto.toEntity() = PhotoEntity(
    albumId = albumId,
    id = id,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl,
)

fun PhotoDto.toPhoto() = Photo(
    albumId = albumId,
    id = id,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl,
)

fun PhotoEntity.toPhoto() = Photo(
    albumId = albumId,
    id = id,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl
)

fun Photo.toEntity() = PhotoEntity(
    albumId = albumId,
    id = id,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl,
)

fun PhotoEntity.toFavouritePhoto() = FavouritePhoto(
    photo = toPhoto(),
    isFavourite = isFavourite
)