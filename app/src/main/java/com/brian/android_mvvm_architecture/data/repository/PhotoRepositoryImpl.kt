package com.brian.android_mvvm_architecture.data.repository

import com.brian.android_mvvm_architecture.data.local.database.dao.PhotoDao
import com.brian.android_mvvm_architecture.data.mapper.toEntity
import com.brian.android_mvvm_architecture.data.mapper.toFavouritePhoto
import com.brian.android_mvvm_architecture.data.model.FavouritePhoto
import com.brian.android_mvvm_architecture.data.remote.api.PhotoService
import com.brian.android_mvvm_architecture.di.Dispatcher
import com.brian.android_mvvm_architecture.di.PhotoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val photoService: PhotoService,
    private val photoDao: PhotoDao,
    @Dispatcher(PhotoDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
) : PhotoRepository {
    override fun getPhotos(): Flow<Result<List<FavouritePhoto>>> {
        return flow {
            val localPhotos = photoDao.getPhotos()
            if (!localPhotos.firstOrNull().isNullOrEmpty()) {
                val localPhotoFlow = localPhotos.map {
                    Result.success(it.map { it.toFavouritePhoto() })
                }
                emitAll(localPhotoFlow)
            } else {
                val remotePhotos = photoService.getPhotos()
                if (remotePhotos.isSuccessful) {
                    val photos = remotePhotos.body()
                    if (photos != null) {
                        photoDao.insertPhotos(photos.map { it.toEntity() })
                        val localPhotoFlow = photoDao.getPhotos().map {
                            Result.success(it.map { it.toFavouritePhoto() })
                        }
                        emitAll(localPhotoFlow)
                    } else {
                        emit(Result.failure(Exception("No photos found")))
                    }
                } else {
                    emit(Result.failure(Exception("Failed to fetch photos")))
                }
            }
        }.flowOn(ioDispatcher)
    }

    // TODO: Consider enhance this function
    override fun getPhotoById(id: Int): Flow<Result<FavouritePhoto>> {
        return flow {
            val photo = photoDao.getPhotoById(id)
            if (photo != null) {
                emit(Result.success(photo.toFavouritePhoto()))
            } else {
                emit(Result.failure(Exception("Photo not found")))
            }
        }.flowOn(ioDispatcher)
    }

    override fun getFavouritePhotos(): Flow<Result<List<FavouritePhoto>>> {
        return photoDao.getFavouritePhotos().map {
            Result.success(it.map { it.toFavouritePhoto() })
        }.catch { e ->
            emit(Result.success(emptyList()))
        }
    }

    override suspend fun updatePhoto(id: Int, isFavourite: Boolean) {
        photoDao.updatePhoto(id, isFavourite)
    }
}
