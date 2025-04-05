package com.brian.android_mvvm_architecture.navigation

const val PHOTO_ID = "photoId"

sealed class Screen(val route: String) {
    object PhotoList : Screen("photo_list")
    object PhotoDetail : Screen("photo_detail/{photoId}") {
        fun createRoute(photoId: Int) = "photo_detail/$photoId"
    }

}