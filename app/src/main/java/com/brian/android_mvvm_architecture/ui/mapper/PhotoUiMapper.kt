package com.brian.android_mvvm_architecture.ui.mapper

import com.brian.android_mvvm_architecture.data.model.Photo
import com.brian.android_mvvm_architecture.ui.photos.PhotoUi


fun Photo.toPhotoUi(isFavourite: Boolean) = PhotoUi(
    id = id,
    title = title,
    imageUrl = url,
    thumbnailUrl = thumbnailUrl,
    isFavourite = isFavourite,
)
