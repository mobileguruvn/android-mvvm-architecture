package com.brian.android_mvvm_architecture.di

import com.brian.android_mvvm_architecture.data.repository.PhotoRepository
import com.brian.android_mvvm_architecture.data.repository.PhotoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindPhotoRepository(
        photoRepositoryImpl: PhotoRepositoryImpl,
    ): PhotoRepository
}