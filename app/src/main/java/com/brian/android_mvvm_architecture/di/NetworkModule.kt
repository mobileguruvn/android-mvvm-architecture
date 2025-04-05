package com.brian.android_mvvm_architecture.di

import com.brian.android_mvvm_architecture.BuildConfig
import com.brian.android_mvvm_architecture.data.remote.api.PhotoService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

const val LOGGING_INTERCEPTOR = "LoggingInterceptor"
const val HEADER_INTERCEPTOR = "HeaderInterceptor"
const val OK_HTTP_CLIENT = "OkHttpClient"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    @Named(LOGGING_INTERCEPTOR)
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    @Named(HEADER_INTERCEPTOR)
    fun provideHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    @Named(OK_HTTP_CLIENT)
    fun provideOkHttpClient(
        @Named(LOGGING_INTERCEPTOR) loggingInterceptor: HttpLoggingInterceptor,
        @Named(HEADER_INTERCEPTOR) headerInterceptor: Interceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(headerInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, @Named(OK_HTTP_CLIENT) okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun providePhotoApi(retrofit: Retrofit): PhotoService =
        retrofit.create(PhotoService::class.java)
}