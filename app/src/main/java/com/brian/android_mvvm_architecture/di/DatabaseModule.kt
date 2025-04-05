package com.brian.android_mvvm_architecture.di

import android.content.Context
import androidx.room.Room
import com.brian.android_mvvm_architecture.data.local.database.PhotoDatabase
import com.brian.android_mvvm_architecture.data.local.database.dao.PhotoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providePhotoDao(photoDatabase: PhotoDatabase): PhotoDao = photoDatabase.photoDao()

    @Provides
    @Singleton
    fun providePhotoDatabase(@ApplicationContext application: Context) = Room.databaseBuilder(
        application,
        PhotoDatabase::class.java,
        "photo_db"
    ).build()
}