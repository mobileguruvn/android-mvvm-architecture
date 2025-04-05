package com.brian.android_mvvm_architecture.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.brian.android_mvvm_architecture.data.local.database.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photos")
    fun getPhotos(): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM photos WHERE id = :id")
    suspend fun getPhotoById(id: Int): PhotoEntity?

    @Query("SELECT * FROM photos WHERE isFavourite = 1")
    fun getFavouritePhotos(): Flow<List<PhotoEntity>>

    @Query("UPDATE photos SET isFavourite = :isFavourite WHERE id = :id")
    suspend fun updatePhoto(id: Int, isFavourite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<PhotoEntity>)

}