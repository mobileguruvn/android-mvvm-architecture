package com.brian.android_mvvm_architecture.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.brian.android_mvvm_architecture.ui.photo_detail.PhotoDetailScreen
import com.brian.android_mvvm_architecture.ui.photos.PhotosScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@Composable
fun PhotoNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.PhotoList.route,
        modifier = modifier
    ) {
        composable(Screen.PhotoList.route) {
            PhotosScreen(
                onPhotoClick = { photoId ->
                    navController.navigate(Screen.PhotoDetail.createRoute(photoId))
                },
            )
        }
        composable(
            route = Screen.PhotoDetail.route,
            arguments = listOf(navArgument(PHOTO_ID) { type = NavType.IntType })
        ) {
            PhotoDetailScreen(navigateUp = {
                navController.navigateUp()
            })
        }
    }
}