package com.brian.android_mvvm_architecture.data.repository

import com.brian.android_mvvm_architecture.data.model.FavouritePhoto
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun getPhotos(): Flow<Result<List<FavouritePhoto>>>

    fun getPhotoById(id: Int): Flow<Result<FavouritePhoto>>

    fun getFavouritePhotos(): Flow<Result<List<FavouritePhoto>>>

    suspend fun updatePhoto(id: Int, isFavourite: Boolean)
}

