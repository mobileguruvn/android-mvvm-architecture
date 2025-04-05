package com.brian.android_mvvm_architecture.data.remote.api

import com.brian.android_mvvm_architecture.data.remote.model.PhotoDto
import retrofit2.Response
import retrofit2.http.GET

interface PhotoService {

    @GET("photos")
    suspend fun getPhotos() : Response<List<PhotoDto>>
}